package quickfix.services

import org.springframework.stereotype.Component
import quickfix.dao.UserRepository
import quickfix.dto.login.LoginDTO
import quickfix.models.User
import quickfix.utils.exceptions.InvalidCredentialsException

@Component
class LoginService (

    val userRepository: UserRepository
) {
    fun loginUser(loginDTO: LoginDTO) {
        val user = findUserByMail(loginDTO.mail)

        if (!validPassword(user, loginDTO)) {
            throw InvalidCredentialsException()
        }
    }

    private fun findUserByMail(mail: String) = userRepository.findByMail(mail)?: throw InvalidCredentialsException()

    private fun validPassword(user: User, loginDTO: LoginDTO): Boolean = user.verifyPassword(loginDTO.password)
}