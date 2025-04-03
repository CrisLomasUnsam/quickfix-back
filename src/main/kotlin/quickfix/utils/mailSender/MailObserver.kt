package quickfix.utils.mailSender
import org.springframework.stereotype.Component

@Component
class MailObserver(val mailSender: SmtpMailSender){
    fun sendRegistrationMailTo(address: String) = mailSender.sendEmail(Mail(to = address))
}
