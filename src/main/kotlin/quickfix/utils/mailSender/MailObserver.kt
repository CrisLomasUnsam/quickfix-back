package quickfix.utils.mailSender
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import quickfix.utils.events.OnRegistrationCompletedEvent

@Component
class MailObserver(val mailSender: IMailSender){
    @EventListener
    fun handleRegistrationCompleted(event: OnRegistrationCompletedEvent) {
        val mail = Mail(
            to = event.user.mail,
            content = """ 
                <html>
                    <body>
                        <p>¡Hola ${event.user.name}!</p>
                    
                        <p>Gracias por registrarte en QuickFix.</p>
                        <p>Para confirmar tu cuenta, haz click en el siguiente link:</p>  
                        <p><a href="${event.confirmationURL}">${event.confirmationURL}</a></p>
           
                        <p>Si no creaste esta cuenta, ignora este mensaje.</p>
                    </body>
                </html>
            """.trimIndent()
        )
        mailSender.sendEmail(mail)
    }
}