package quickfix.utils.mailSender

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class SmtpMailSender(
    private val javaMailSender: JavaMailSender

) : IMailSender {

    override fun sendEmail(mails: List<Mail>) {
        mails.forEach { mail ->
            try {
                val message: MimeMessage = javaMailSender.createMimeMessage()
                val helper = MimeMessageHelper(message, true)

                helper.setFrom(mail.from)
                helper.setTo(mail.to)
                helper.setSubject(mail.subject)
                helper.setText(mail.content, true)
                javaMailSender.send(message)
                println("Email enviado a ${mail.to}")

            } catch (e: Exception) {
                println("Error: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
