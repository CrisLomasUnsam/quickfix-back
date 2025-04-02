package services

import dto.register.*
import exceptions.BusinessException
import org.springframework.stereotype.Service
import utils.mailSender.MailObserver

@Service
class RegisterService (private val mailObserver: MailObserver) {

    fun registerCustomer(registerData: RegisterRequestDTO): RegisterRequestDTO = registrationProcess(registerData)

    fun registerProfessional(registerData: RegisterRequestDTO): RegisterRequestDTO = registrationProcess(registerData)

    fun validRegisterData(registerData: RegisterRequestDTO): Boolean = registerData.email.isNotEmpty() && registerData.password.isNotEmpty()
    
    fun registrationProcess(registerData: RegisterRequestDTO): RegisterRequestDTO {
        if (!this.validRegisterData(registerData)) throw BusinessException("Invalid data")
        mailObserver.sendRegistrationMailTo(registerData.email.trim())
        return registerData
    }
}