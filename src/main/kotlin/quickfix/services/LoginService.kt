package quickfix.services

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import quickfix.dao.UserRepository
import quickfix.dto.login.LoginDTO
import quickfix.utils.exceptions.InvalidCredentialsException

@Component
class LoginService (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun loginUser(loginDTO: LoginDTO) {
        val user = findUserByMail(loginDTO.mail)

        if (!passwordEncoder.matches(loginDTO.password, user.password)) {
            throw InvalidCredentialsException()
        }

//        retornar JWT aquí
    }

    private fun findUserByMail(mail: String) = userRepository.findByMail(mail)?: throw InvalidCredentialsException()
}