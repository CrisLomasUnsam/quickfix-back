package quickfix.services

import org.springframework.stereotype.Component
import quickfix.dao.CustomerRepository
import quickfix.dao.ProfessionalRepository
import quickfix.dto.login.LoginDTO

@Component
class LoginService (
    val professionalRepository: ProfessionalRepository,
    val customerRepository: CustomerRepository
) {
    fun loginCustomer(loginDTO: LoginDTO) {

    }

    fun loginProfessional(loginDTO: LoginDTO) {

    }
}