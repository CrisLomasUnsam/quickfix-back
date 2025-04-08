package quickfix.services

import org.springframework.stereotype.Service

import quickfix.dao.UserRepository
import quickfix.dto.register.RegisterRequestDTO

import quickfix.dto.register.toUser
import quickfix.models.Customer
import quickfix.models.Professional
import quickfix.utils.mailSender.MailObserver

@Service
class RegisterService (
    private val mailObserver: MailObserver,
    private val userRepository: UserRepository,
) {

    //TODO: Este método va a tener que refactorizarse cuando implementemos Hibernate
    fun registerUser(registerData: RegisterRequestDTO) {

        //Cuando tengamos Hibernate: val userInfo = usuarioInfoRepository.save(registerData.toUserInfo())
        val userInfo = registerData.toUser()

        if (registerData.isProfessional) {
            userInfo.professional = Professional()
        } else
            userInfo.customer = Customer()
        userRepository.create(userInfo)

        mailObserver.sendRegistrationMailTo(userInfo.mail)
    }


}