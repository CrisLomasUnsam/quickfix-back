package quickfix.utils.mailSender

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class SmtpMailSender(
    private val javaMailSender: JavaMailSender

) : IMailSender {

    override fun sendEmail(mail: List<Mail>) {
        mail.forEach {
            try {
                val message: MimeMessage = javaMailSender.createMimeMessage()
                val helper = MimeMessageHelper(message, true)

                helper.setFrom(it.from)
                helper.setTo(it.to)
                helper.setSubject(it.subject)
                helper.setText(it.content, true)
                javaMailSender.send(message)
                //println("Email enviado a ${it.to}")

            } catch (e: Exception) {
                println("Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
