package quickfix.services

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import quickfix.dao.UserRepository
import quickfix.dto.register.RegisterRequestDTO

import quickfix.dto.register.toUser
import quickfix.utils.exceptions.BusinessException
import quickfix.utils.mailSender.MailObserver
import java.sql.SQLException

@Service
class RegisterService(
    private val mailObserver: MailObserver,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun registerUser(registerData: RegisterRequestDTO) = userExists(registerData.mail) ?: registerNewUser(registerData)

    private fun userExists(mail: String) = userRepository.findByMail(mail)?.let { throw BusinessException("El usuario con mail ${it.mail} ya existe") }

    private fun registerNewUser(registerData: RegisterRequestDTO) {
        val user = registerData.toUser().apply { this.encodedPassword = encodePassword(this.encodedPassword) }
        userRepository.save(user)
        mailObserver.sendRegistrationMailTo(user.mail)
    }

    private fun encodePassword(password: String) = passwordEncoder.encode(password)
}