package quickfix.services

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.TokenRepository
import quickfix.dao.UserRepository
import quickfix.dto.register.RegisterRequestDTO
import quickfix.dto.register.toUser
import quickfix.models.Token
import quickfix.models.User
import quickfix.utils.CONFIRM_FRONTEND_URL
import quickfix.utils.FRONTEND_URL
import quickfix.utils.events.OnRegistrationCompletedEvent
import quickfix.utils.exceptions.BusinessException
import quickfix.utils.exceptions.InvalidCredentialsException
import java.sql.SQLException
import java.time.LocalDateTime

@Service
class RegisterService(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val tokenRepository: TokenRepository
) {
    fun getVerificationToken(token: String) = tokenRepository.findByValue(token)
        ?: throw InvalidCredentialsException()

    fun getUserByMail(mail: String): User =
        userRepository.findByMail(mail).orElseThrow{ BusinessException("Usuario no encontrado $mail") }

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun registerUser(registerData: RegisterRequestDTO){
        validateUserAlreadyExists(registerData.mail)
        registerNewUser(registerData)
    }

    private fun validateUserAlreadyExists(mail: String) {
        val user = userRepository.findByMail(mail)
        if(user.isPresent) throw BusinessException("El usuario con mail $mail ya existe")
    }

    private fun registerNewUser(registerData: RegisterRequestDTO) {
        val user : User = registerData.toUser().apply { setNewPassword(registerData.rawPassword) }
        val savedUser = userRepository.save(user) /*Se persiste un usuario sin verificar aun pero cuyo mail no esta en la bbdd*/
        val token = tokenRepository.save(Token.createTokenEntity(savedUser))
        val confirmationURL = createConfirmationLink(token.value)
        eventPublisher.publishEvent(OnRegistrationCompletedEvent(savedUser, confirmationURL))
    }

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun validateUserByToken(token: String) {
        if (token.isBlank()) { throw BusinessException("Token invalido") }
        val verificationToken = getVerificationToken(token)
        val user = verificationToken.user
        val savedUser = getUserByMail(user.mail)
        if (verificationToken.expiryDate.isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken)
            throw BusinessException("Token expirado")
        }
        savedUser.verified = true
        tokenRepository.delete(verificationToken)
    }

    private fun createConfirmationLink(token: String) = "$FRONTEND_URL+$CONFIRM_FRONTEND_URL?token=$token"
}