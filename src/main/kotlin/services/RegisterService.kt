package services

import dto.register.RegisterRequestDTO
import org.springframework.stereotype.Service
import utils.mailSender.MailObserver

@Service
class RegisterService (private val mailObserver: MailObserver) {

    fun registerUser(registerData: RegisterRequestDTO): Any {
        if (!this.validRegisterData(registerData)) {
            throw Exception("Invalid data") /*Cambiar cuando esten las exceptions*/
        }
        mailObserver.sendRegistrationMailTo(registerData.email.trim())
        return TODO("crear una instancia de User con apply de password e email")
    }

    fun validRegisterData(registerData: RegisterRequestDTO): Boolean {
        return registerData.email.isNotEmpty() && registerData.password.isNotEmpty()
    }
}