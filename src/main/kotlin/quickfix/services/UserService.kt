package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.UserRepository
import quickfix.dto.job.jobRequest.CancelJobRequestDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val redisService: RedisService,
    private val professionService: ProfessionService,

) {
    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow{ BusinessException("Usuario no encontrado") }

    fun assertUserExists(id: Long) {
        if (!userRepository.existsById(id)) {
            throw BusinessException("Usuario no encontrado: $id")
        }
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
        val profession = professionService.getProfessionById(jobRequest.professionId)
        redisService.requestJob(jobRequest, profession.id)
    }

    fun getJobOffers(customerId : Long) =
        redisService.getJobOffers(customerId)

    fun cancelJobRequest (cancelJobRequest : CancelJobRequestDTO) {
        val (customerId, professionId) = cancelJobRequest
        this.assertUserExists(customerId)
        professionService.getProfessionById(professionId)
        redisService.removeJobRequest(customerId, professionId)
    }
}