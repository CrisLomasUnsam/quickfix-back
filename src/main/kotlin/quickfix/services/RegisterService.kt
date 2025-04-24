package quickfix.services

import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.TokenRepository
import quickfix.dao.UserRepository
import quickfix.dto.register.RegisterRequestDTO
import quickfix.dto.register.toUser
import quickfix.models.User
import quickfix.models.RegisterToken
import quickfix.utils.CONFIRM_FRONTEND_URL
import quickfix.utils.FRONTEND_URL
import quickfix.utils.events.OnRegistrationCompletedEvent
import quickfix.utils.exceptions.BusinessException
import quickfix.utils.exceptions.InvalidCredentialsException
import java.sql.SQLException
import java.time.LocalDateTime
import java.util.*

@Service
class RegisterService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val eventPublisher: ApplicationEventPublisher,
    private val tokenRepository: TokenRepository
) {
    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun registerUser(registerData: RegisterRequestDTO) = userExists(registerData.mail) ?: registerNewUser(registerData)

    private fun userExists(mail: String) = userRepository.findByMail(mail)?.let { throw BusinessException("El usuario con mail ${it.mail} ya existe") }

    private fun registerNewUser(registerData: RegisterRequestDTO) {
        val user = registerData.toUser().apply { this.password = encodePassword(this.password) }
        val savedUser = userRepository.save(user) /*Se persiste un usuario sin verificar aun pero cuyo mail no esta en la bbdd*/
        val token = UUID.randomUUID().toString()
        this.createVerificationTokenEntity(savedUser, token)
        val confirmationURL = createConfirmationLink(token)
        eventPublisher.publishEvent(OnRegistrationCompletedEvent(savedUser, confirmationURL))
    }

    private fun createConfirmationLink(token: String) = "$FRONTEND_URL+$CONFIRM_FRONTEND_URL?token=$token"

    private fun encodePassword(password: String) = passwordEncoder.encode(password)

    private fun createVerificationTokenEntity(user: User, token: String) {
        val newToken = RegisterToken().apply { this.user = user; this.token = token }
        tokenRepository.save(newToken)
    }

    private fun getVerificationToken(token: String) = tokenRepository.findByToken(token)

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun validateUserByToken(token: String) {
        if (token.isBlank()) { throw BusinessException("Token invalido") }
        val verificationToken = getVerificationToken(token) ?: throw InvalidCredentialsException()
        val user = verificationToken.user
        val savedUser = userRepository.findByMail(user.mail) ?: throw BusinessException("El usuario asociado al token no existe")
        if (verificationToken.expiryDate.isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken)
            throw BusinessException("Token expirado")
        }
        savedUser.verified = true
        tokenRepository.delete(verificationToken)
    }
}