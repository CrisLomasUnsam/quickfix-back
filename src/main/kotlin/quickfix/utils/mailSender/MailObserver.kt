package quickfix.utils.mailSender
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import quickfix.utils.events.OnChangePasswordRequestEvent
import quickfix.utils.events.OnRegistrationCompletedEvent

@Component
class MailObserver(val mailSender: IMailSender){

    @EventListener
    fun handleRegistrationCompleted(event: OnRegistrationCompletedEvent) {
        val to = event.user.mail
        val content = """ 
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

        val mail = Mail.toRegistrationMail(to, content)
        mailSender.sendEmail(mail)
    }

    @EventListener
    fun handleRequestPasswordRecovery(event: OnChangePasswordRequestEvent) {
        val to = event.user.mail
        val content = """ 
                <html>
                    <body>
                        <p>¡Hola ${event.user.name}!</p>
                    
                        <p>Solicitaste cambiar tu contraseña.</p>
                        <p>Para ingresar una contraseña nueva, haz click en el siguiente link:</p>  
                        <p><a href="${event.recoveryURL}">${event.recoveryURL}</a></p>
           
                        <p>Si no has solicitado cambiar tu contraseña, ignora este mensaje.</p>
                    </body>
                </html>
            """.trimIndent()

        val mail = Mail.toPasswordRecoveryMail(to, content)
        mailSender.sendEmail(mail)
    }
}