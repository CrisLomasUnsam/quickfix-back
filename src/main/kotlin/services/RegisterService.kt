package services

import dto.register.RegisterRequestDTO
import exceptions.BusinessException
import org.springframework.stereotype.Service
import utils.mailSender.MailObserver

@Service
class RegisterService (private val mailObserver: MailObserver) {

    fun registerCustomer(registerData: RegisterRequestDTO) = registrationProcess(registerData)

    fun registerProfessional(registerData: RegisterRequestDTO) = registrationProcess(registerData)

    fun validRegisterData(registerData: RegisterRequestDTO): Boolean = registerData.email.isNotEmpty() && registerData.password.isNotEmpty()
    
    fun registrationProcess(registerData: RegisterRequestDTO){
        if (!this.validRegisterData(registerData)) throw BusinessException("Invalid data")
        mailObserver.sendRegistrationMailTo(registerData.email.trim())
    }
}