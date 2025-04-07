package quickfix.services

import org.springframework.stereotype.Component
import quickfix.dao.CustomerRepository
import quickfix.dao.ProfessionalRepository
import quickfix.dto.login.LoginDTO
import quickfix.utils.exceptions.InvalidCredentialsException

@Component
class LoginService (
    val professionalRepository: ProfessionalRepository,
    val customerRepository: CustomerRepository
) {
    fun loginCustomer(loginDTO: LoginDTO) {
        val customer = customerRepository.findByMail(loginDTO.mail)
        if (customer == null || !customer.info.verifyPassword(loginDTO.password))
            throw InvalidCredentialsException()
    }

    fun loginProfessional(loginDTO: LoginDTO) {
        val professional = professionalRepository.findByMail(loginDTO.mail)
        if (professional == null || !professional.info.verifyPassword(loginDTO.password))
            throw InvalidCredentialsException()

    }
}