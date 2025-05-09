package quickfix.services

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.TokenRepository
import quickfix.dao.UserRepository
import quickfix.dto.user.NewCredentialRequestDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.Profession
import quickfix.models.ProfessionalInfo
import quickfix.models.Token
import quickfix.models.User
import quickfix.utils.FRONTEND_URL
import quickfix.utils.RECOVERY_FRONTEND_URL
import quickfix.utils.events.OnChangePasswordRequestEvent
import quickfix.utils.exceptions.BusinessException
import quickfix.utils.exceptions.InvalidCredentialsException
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val tokenRepository: TokenRepository
) {

    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow{ BusinessException("Usuario no encontrado $id") }

    fun getUserByMail(mail: String): User =
        userRepository.findByMail(mail).orElseThrow{ BusinessException("Usuario no encontrado $mail") }

    fun getVerificationToken(token: String) = tokenRepository.findByValue(token)
        ?: throw InvalidCredentialsException()


    fun assertUserExists(id: Long) {
        if (!userRepository.existsById(id))
            throw BusinessException("Usuario no encontrado: $id")
    }

    fun getProfessionalInfo(userId: Long) : ProfessionalInfo =
        userRepository.findUserWithProfessionalInfoById(userId).orElseThrow{ BusinessException() }.professionalInfo

    fun getActiveProfessionsByUserId(id: Long): Set<Profession> =
        getProfessionalInfo(id)
            .professionalProfessions
            .filter { it.active }
            .map { it.profession }
            .toSet()

    @Transactional(rollbackFor = [Exception::class])
    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = this.getUserById(id)
        user.updateUserInfo(modifiedInfo)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun changeUserPassword(mail: String) {
        val user = getUserByMail(mail)
        val token = tokenRepository.save(Token.createTokenEntity(user))
        val recoveryURL = createRecoveryURL(token.value)
        eventPublisher.publishEvent(OnChangePasswordRequestEvent(user, recoveryURL))

    }

    @Transactional(rollbackFor = [Exception::class])
    fun validateUserByToken(newCredential: NewCredentialRequestDTO) {
        val token = newCredential.token
        if (token.isBlank()) { throw BusinessException("Token invalido") }
        val verificationToken = getVerificationToken(token)
        val user = verificationToken.user
        val userToUpdate = getUserByMail(user.mail)
        if (verificationToken.expiryDate.isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken)
            throw BusinessException("Token expirado")
        }
        userRepository.save(userToUpdate.apply { setNewPassword(newCredential.newRawPassword) })
        tokenRepository.delete(verificationToken)
    }

    private fun createRecoveryURL(token: String): String = "$FRONTEND_URL+$RECOVERY_FRONTEND_URL?token=$token"

}