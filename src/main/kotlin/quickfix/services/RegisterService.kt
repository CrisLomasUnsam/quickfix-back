package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.CustomerRepository
import quickfix.dao.ProfessionalRepository
import quickfix.dao.UserInfoRepository
import quickfix.dto.register.RegisterRequestDTO
import quickfix.dto.register.toCustomer
import quickfix.dto.register.toProfessional
import quickfix.dto.register.toUserInfo
import quickfix.utils.mailSender.MailObserver

@Service
class RegisterService (
    private val mailObserver: MailObserver,
    private val userInfoRepository: UserInfoRepository,
    private val customerRepository: CustomerRepository,
    private val professionalRepository: ProfessionalRepository
) {

    //TODO: Este método va a tener que refactorizarse cuando implementemos Hibernate
    fun registerUser(registerData: RegisterRequestDTO) {

        //Cuando tengamos Hibernate: val userInfo = usuarioInfoRepository.save(registerData.toUserInfo())
        val userInfo = registerData.toUserInfo()
        userInfoRepository.create(userInfo)

        val customer = registerData.toCustomer()
        customerRepository.create(customer)

        val professional = registerData.toProfessional()
        professionalRepository.create(professional)

        mailObserver.sendRegistrationMailTo(userInfo.mail)
    }


}