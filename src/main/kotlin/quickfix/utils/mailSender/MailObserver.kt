package quickfix.utils.mailSender
import org.springframework.stereotype.Component

@Component
class MailObserver(val mailSender: IMailSender){
    fun sendRegistrationMailTo(address: String) = mailSender.sendEmail(Mail(to = address))
}
