package quickfix.services

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.AddressRepository
import quickfix.dao.TokenRepository
import quickfix.dto.register.RegisterRequestDTO
import quickfix.dto.register.toUser
import quickfix.dto.register.NewCredentialRequestDTO
import quickfix.models.Address
import quickfix.models.Token
import quickfix.models.User
import quickfix.utils.FRONTEND_URL
import quickfix.utils.events.OnRegistrationCompletedEvent
import quickfix.utils.exceptions.IllegalDataException
import quickfix.utils.exceptions.InvalidCredentialsException
import quickfix.utils.exceptions.InvalidTokenException
import java.sql.SQLException
import java.time.LocalDateTime

@Service
class RegisterService(
    private val userService: UserService,
    private val eventPublisher: ApplicationEventPublisher,
    private val tokenRepository: TokenRepository,
    private val addressRepository: AddressRepository
) {
    fun getVerificationToken(token: String) = tokenRepository.findByValue(token)
        ?: throw InvalidCredentialsException()

    fun getUserByMail(mail: String): User = userService.getByMail(mail)

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun registerUser(registerData: RegisterRequestDTO){
        validateUserAlreadyExists(registerData.mail)
        registerNewUser(registerData)
    }

    private fun validateUserAlreadyExists(mail: String) {
        val user = userService.findByMail(mail)
        if(user.isPresent) throw IllegalDataException("El usuario con mail $mail ya existe")
    }

    private fun registerNewUser(registerData: RegisterRequestDTO) {
        val user : User = registerData.toUser().apply { setNewPassword(registerData.rawPassword) }
        val savedUser = userService.save(user)
        val address = Address().apply {
            this.alias = "Principal"
            this.user = savedUser
            this.principal = true
            this.streetAddress = registerData.streetAddress
            this.streetReference = registerData.streetReference?: ""
            this.zipCode = registerData.zipCode
            this.state = registerData.state
            this.city = registerData.city
        }
        addressRepository.save(address)
        val token = tokenRepository.save(Token.createTokenEntity(savedUser))
        val confirmationURL = createConfirmationOrRecoveryLink(token.value)
        eventPublisher.publishEvent(OnRegistrationCompletedEvent(savedUser, confirmationURL))
    }

    private fun verifyToken(token : String) : Token{
        if (token.isBlank()) { throw InvalidTokenException() }
        val verificationToken = getVerificationToken(token)
        if (verificationToken.expiryDate.isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken)
            throw InvalidTokenException()
        }
        return verificationToken
    }

    @Transactional(rollbackFor = [Exception::class])
    fun validateUserByToken(token: String) {
        val verifiedToken = verifyToken(token)
        val user = getUserByMail(verifiedToken.user.mail)
        user.verified = true
        tokenRepository.delete(verifiedToken)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun recoveryPassword(newCredential: NewCredentialRequestDTO) {
        val token = newCredential.token
        val verifiedToken = verifyToken(token)
        val userToUpdate = getUserByMail(verifiedToken.user.mail)
        userService.save(userToUpdate.apply { setNewPassword(newCredential.newRawPassword) })
        tokenRepository.delete(verifiedToken)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun requestChangeUserPassword(mail: String) = userService.requestChangePassword(mail)

    private fun createConfirmationOrRecoveryLink(token: String) = "$FRONTEND_URL/emailConfirmation?token=$token"
}