package quickfix.services

import org.springframework.stereotype.Component
import quickfix.dao.UserRepository
import quickfix.dto.login.LoginDTO
import quickfix.utils.exceptions.InvalidCredentialsException

@Component
class LoginService (

    val userRepository: UserRepository
) {
    fun loginUser(loginDTO: LoginDTO) {
        val user = userRepository.findByMail(loginDTO.mail)
        if (user == null || !user.verifyPassword(loginDTO.password))
            throw InvalidCredentialsException()
    }

}