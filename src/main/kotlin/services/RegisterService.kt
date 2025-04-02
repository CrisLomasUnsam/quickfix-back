package services

import dto.register.*
import models.Customer
import models.Professional
import org.springframework.stereotype.Service
import utils.mailSender.MailObserver

@Service
class RegisterService (private val mailObserver: MailObserver) {

    fun registerCustomer(registerData: CustomerRegisterRequestDTO): Customer = customerRegistration(registerData)

    fun registerProfessional(registerData: ProfessionalRegisterRequestDTO): Professional = professionalRegistration(registerData)


    fun customerRegistration(registerData: CustomerRegisterRequestDTO): Customer {
        val customer = registerData.fromDTO()
        mailObserver.sendRegistrationMailTo(customer.mail)
        return customer
    }

    fun professionalRegistration(registerData: ProfessionalRegisterRequestDTO): Professional {
        val professional = registerData.fromDTO()
        mailObserver.sendRegistrationMailTo(professional.mail)
        return professional
    }
}