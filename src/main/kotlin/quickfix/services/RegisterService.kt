package quickfix.services

import org.springframework.stereotype.Service

import quickfix.dao.UserRepository
import quickfix.dto.register.RegisterRequestDTO

import quickfix.dto.register.toUser
import quickfix.utils.mailSender.MailObserver

@Service
class RegisterService (
    private val mailObserver: MailObserver,
    private val userRepository: UserRepository,
) {

    fun registerUser(registerData: RegisterRequestDTO) {
        val user = registerData.toUser()
        userRepository.save(user)
        mailObserver.sendRegistrationMailTo(user.mail)
    }


}