package services

import dto.register.*
import models.Customer
import models.Professional
import org.springframework.stereotype.Service
import utils.mailSender.MailObserver

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