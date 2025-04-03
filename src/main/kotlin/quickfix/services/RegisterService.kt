package quickfix.services

import quickfix.models.Customer
import quickfix.models.Professional
import org.springframework.stereotype.Service
import quickfix.dto.register.RegisterRequestDTO
import quickfix.dto.register.toCustomer
import quickfix.dto.register.toProfessional
import quickfix.utils.mailSender.MailObserver

@Service
class RegisterService (private val mailObserver: MailObserver) {

    fun registerCustomer(registerData: RegisterRequestDTO): Customer {
        val customer = registerData.toCustomer()
        mailObserver.sendRegistrationMailTo(customer.mail)
        return customer
    }

    fun registerProfessional(registerData: RegisterRequestDTO): Professional {
        val professional = registerData.toProfessional()
        mailObserver.sendRegistrationMailTo(professional.mail)
        return professional
    }

}