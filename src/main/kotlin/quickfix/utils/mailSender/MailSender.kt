package quickfix.utils.mailSender

import org.springframework.stereotype.Component

@Component
class SmtpMailSender : IMailSender {
    override fun sendEmail(mail: Mail) {
        println("Enviando mail a ${mail.to} desde ${mail.from} con asunto '${mail.subject}'")
    }
}
