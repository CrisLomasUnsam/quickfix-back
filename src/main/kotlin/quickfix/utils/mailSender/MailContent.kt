package quickfix.utils.mailSender

import quickfix.utils.enums.MailType
import quickfix.utils.events.OnChangePasswordRequestEvent
import quickfix.utils.events.OnRegistrationCompletedEvent

class RegistrationMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): Mail {
        val e = event as OnRegistrationCompletedEvent
        val content = buildHtml("""
            <p>Hola ${e.user.name},</p>
            <p>Gracias por registrarte. Confirmá tu cuenta aquí:</p>
            <p><a href=\"${e.confirmationURL}\">${e.confirmationURL}</a></p>
            
            <p>Si no creaste esta cuenta, ignorá este mensaje.</p>
        """.trimIndent())
        return Mail.toNotificationMail(e.user.mail, MailType.REGISTRATION_CONFIRMATION.subject, content)
    }
}

class PasswordRecovery : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): Mail {
        val e = event as OnChangePasswordRequestEvent
        val content = buildHtml("""
            <p>Hola ${e.user.name},</p>
            <p>Para recuperar tu contraseña, hacé click aquí:</p>
            <p><a href=\"${e.recoveryURL}\">${e.recoveryURL}</a></p>
            
            <p>Si no has solicitado cambiar tu contraseña, ignorá este mensaje.</p>
        """.trimIndent())
        return Mail.toNotificationMail(e.user.mail, MailType.PASSWORD_RECOVERY.subject, content)
    }
}