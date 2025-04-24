package quickfix.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.UserRepository
import quickfix.dto.job.jobRequest.CancelJobRequestDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.login.LoginDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.Profession
import quickfix.models.ProfessionalInfo
import quickfix.models.User
import quickfix.security.JwtTokenUtils
import quickfix.utils.exceptions.BusinessException
import quickfix.utils.exceptions.InvalidCredentialsException

@Service
class UserService(

    private val userRepository: UserRepository,
    private val redisService: RedisService,
    private val professionService: ProfessionService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenUtils: JwtTokenUtils

) : UserDetailsService {

    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow{ BusinessException("Usuario no encontrado $id") }

    //fun getUserByMail(mail: String): User = userRepository.findByMail(mail) ?: throw BusinessException("Usuario no encontrado $mail")

    fun assertUserExists(id: Long) {
        if (!userRepository.existsById(id))
            throw BusinessException("Usuario no encontrado: $id")
    }

    fun getProfessionalInfo(userId: Long) : ProfessionalInfo {
        val user = userRepository.findUserWithProfessionalInfoById(userId).orElseThrow{ BusinessException() }
        return user.professionalInfo
    }

    @Transactional(rollbackFor = [Exception::class])
    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = this.getUserById(id)
        user.updateUserInfo(modifiedInfo)
    }

    fun getProfessionsByUserId(id: Long): Set<Profession> {
        val info = userRepository.findUserProfessionsById(id)
            ?: throw BusinessException("Usuario o ProfessionalInfo no encontrado")
        return info.professionalInfo.professions
    }

    fun requestJob(jobRequest : JobRequestDTO) {
        this.assertUserExists(jobRequest.customerId)
        professionService.assertProfessionExists(jobRequest.professionId)
        redisService.requestJob(jobRequest, jobRequest.professionId)
    }

    fun getJobOffers(customerId : Long) =
        redisService.getJobOffers(customerId)

    fun cancelJobRequest (cancelJobRequest : CancelJobRequestDTO) {
        val (customerId, professionId) = cancelJobRequest
        this.assertUserExists(customerId)
        professionService.getProfessionById(professionId)
        redisService.removeJobRequest(customerId, professionId)
    }

    /* JWT */

    @Transactional(readOnly = true)
    fun login(loginDTO: LoginDTO): String {
        val user = userRepository.findByMail(loginDTO.mail)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(loginDTO.password, user.password)) {
            throw InvalidCredentialsException()
        }

        return jwtTokenUtils.createToken(user.mail, listOf()) // agregar roles si los tenés
    }

    @Transactional
    fun validateUser(mail: String): User = userRepository.findByMail(mail)
        ?: throw InvalidCredentialsException()

    @Transactional
    override fun loadUserByUsername(mail: String?): UserDetails {
        if (mail.isNullOrEmpty()) throw InvalidCredentialsException()
        val user = validateUser(mail)
        return user.buildUser()
    }
}