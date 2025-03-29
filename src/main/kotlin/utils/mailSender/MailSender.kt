package utils.mailSender

import org.springframework.stereotype.Component

@Component
interface MailSender {
    fun sendEmail(mail: Mail)
}

@Component
data class Mail(
    val from: String = "registration@quickfix.com",
    var to: String,
    val subject: String = "Confirm your account",
    var content: String = "Confirm your account by clicking on the link below",
)

@Component
class MailObserver(val mailSender: MailSender){
    fun sendRegistrationMailTo(address: String) {
        mailSender.sendEmail(Mail(to = address))
    }

}
