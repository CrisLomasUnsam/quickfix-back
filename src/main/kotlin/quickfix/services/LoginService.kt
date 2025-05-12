package quickfix.services

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import quickfix.dto.login.LoginDTO
import quickfix.models.Role
import quickfix.models.User
import quickfix.security.JwtTokenUtils
import quickfix.utils.exceptions.InvalidCredentialsException

@Component
class LoginService (
    private val userService: UserService,
    private val jwtTokenUtils: JwtTokenUtils
) {

    @Transactional(readOnly = true)
    fun loginAsCustomer(loginDTO: LoginDTO): String {
        val user = getUser(loginDTO)
        return jwtTokenUtils.createToken(user.id, Role.CUSTOMER.roleName)!!
    }

    @Transactional(readOnly = true)
    fun loginAsProfessional(loginDTO: LoginDTO): String {
        val user = getUser(loginDTO)
        return jwtTokenUtils.createToken(user.id, Role.PROFESSIONAL.roleName)!!
    }

    private fun getUser(loginDTO: LoginDTO): User {
        val user = userService.getByMail(loginDTO.mail)

        if (!user.verifyPassword(loginDTO.password))
            throw InvalidCredentialsException()

        return user
    }
}