package quickfix.services

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.UserRepository
import quickfix.dto.login.LoginDTO
import quickfix.models.User
import quickfix.security.JwtTokenUtils
import quickfix.utils.exceptions.InvalidCredentialsException

@Component
class LoginService (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenUtils: JwtTokenUtils
) {

    /* JWT */

    @Transactional(readOnly = true)
    fun login(loginDTO: LoginDTO): String {
        val user = validUser(loginDTO.mail)

        if (!passwordEncoder.matches(loginDTO.password, user.password))
            throw InvalidCredentialsException()

        return jwtTokenUtils.createToken(user.mail, user.roles.map { it.name })!! // agregar roles si los tenés
    }

    private fun validUser(mail: String): User = userRepository.findByMail(mail)
        ?: throw InvalidCredentialsException()
}