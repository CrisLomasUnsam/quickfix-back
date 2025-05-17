package quickfix.services

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import quickfix.dao.TokenRepository
import quickfix.dao.UserRepository
import quickfix.dto.user.ISeeUserProfile
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.Profession
import quickfix.models.ProfessionalInfo
import quickfix.models.Token
import quickfix.models.User
import quickfix.utils.FRONTEND_URL
import quickfix.utils.events.OnChangePasswordRequestEvent
import quickfix.utils.exceptions.NotFoundException
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val tokenRepository: TokenRepository
) {

    private fun createRecoveryURL(token: String) = "$FRONTEND_URL/confirm?token=$token"

    fun getAvatar(userId: Long): ByteArray = this.getById(userId).avatar

    fun getById(id: Long): User =
        userRepository.findById(id).orElseThrow{ NotFoundException("Usuario no encontrado $id") }

    fun findByMail(mail : String) : Optional<User> =
        userRepository.findByMail(mail)

    fun getByMail(mail: String): User =
        userRepository.findByMail(mail).orElseThrow{ NotFoundException("Usuario no encontrado $mail") }

    fun save(user : User) = userRepository.save(user)

    fun assertUserExists(id: Long) {
        if (!userRepository.existsById(id))
            throw NotFoundException("Usuario no encontrado: $id")
    }

    fun getProfessionalInfo(userId: Long) : ProfessionalInfo =
        userRepository.findUserWithProfessionalInfoById(userId).orElseThrow{ NotFoundException() }.professionalInfo

    fun getActiveProfessionsByUserId(id: Long): Set<Profession> =
        getProfessionalInfo(id).getActiveProfessions()

    fun getSeeProfessionalProfileInfo(professionalId : Long) : ISeeUserProfile =
        userRepository.getSeeProfessionalProfileInfo(professionalId)

    fun getSeeCustomerProfileInfo(customerId : Long) : ISeeUserProfile =
        userRepository.getSeeCustomerProfileInfo(customerId)

    @Transactional(rollbackFor = [Exception::class])
    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = this.getById(id)
        user.updateUserInfo(modifiedInfo)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun requestChangePassword(mail: String) {
        val user = getByMail(mail)
        val token = tokenRepository.save(Token.createTokenEntity(user))
        val recoveryURL = createRecoveryURL(token.value)
        eventPublisher.publishEvent(OnChangePasswordRequestEvent(user, recoveryURL))

    }

    @Transactional(rollbackFor = [Exception::class])
    fun updateAvatar(currentUserId: Long, file: MultipartFile) {
        val user = this.getById(currentUserId)
        user.avatar = file.bytes
    }
}