package quickfix.utils.mailSender

import quickfix.utils.enums.MailType
import quickfix.utils.events.*

class RegistrationMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnRegistrationCompletedEvent
        val content = buildHtml("""
            <p>Hola ${e.user.name},</p>
            <p>Gracias por registrarte. Confirmá tu cuenta aquí:</p>
            <p><a href=\"${e.confirmationURL}\">${e.confirmationURL}</a></p>
            
            <p>Si no creaste esta cuenta, ignorá este mensaje.</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.user.mail, MailType.REGISTRATION_CONFIRMATION.subject, content)
        )
    }
}

class PasswordRecoveryMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnChangePasswordRequestEvent
        val content = buildHtml("""
            <p>Hola ${e.user.name},</p>
            <p>Para recuperar tu contraseña, hacé click aquí:</p>
            <p><a href=\"${e.recoveryURL}\">${e.recoveryURL}</a></p>
            
            <p>Si no has solicitado cambiar tu contraseña, ignorá este mensaje.</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.user.mail, MailType.PASSWORD_RECOVERY.subject, content)
        )
    }
}

class JobStartedMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnJobStartedEvent
        val content = buildHtml("""
            <p>El trabajo de ${e.job.profession.name} ha sido iniciado.</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.job.professional.mail, MailType.JOB_STARTED.subject, content),
            Mail.toNotificationMail(e.job.customer.mail, MailType.JOB_STARTED.subject, content)
        )
    }
}

class JobRequestedMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnJobRequestedEvent
        val content = buildHtml(
            """
            <p>Hola ${e.request.customer.name},</p>
            <p>Has solicitado un trabajo: "${e.request.detail}".</p>
        """.trimIndent()
        )
        return listOf(
            Mail.toNotificationMail(e.request.customer.mail, MailType.JOB_REQUESTED.subject, content)
        )
    }
}

class JobOfferedMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnJobOfferedEvent
        val content = buildHtml("""
            <p>Hola ${e.offer.professional.name},</p>
            <p>Has ofertado la solicitud de ${e.offer.request.customer.name}.</p>
            <p>Precio ofertado: $${e.offer.price}.</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.offer.professional.mail, MailType.JOB_OFFERED.subject, content)
        )
    }
}

class JobAcceptedMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnJobAcceptedEvent
        val content = buildHtml("""
            <p>Hola ${e.job.professional.name},</p>
            <p>Tu oferta para el trabajo de ${e.job.profession.name} ha sido aceptada.</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.job.professional.mail, MailType.JOB_ACCEPTED.subject, content)
        )
    }
}

class JobEndedMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnJobEndedEvent
        val content = buildHtml("""
            <p>El trabajo de ${e.job.profession.name} ha finalizado.</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.job.professional.mail, MailType.JOB_ENDED.subject, content),
            Mail.toNotificationMail(e.job.customer.mail, MailType.JOB_ENDED.subject, content)
        )
    }
}

class JobCanceledMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnJobCanceledEvent
        val content = buildHtml("""
            <p>El trabajo de ${e.job.profession.name} ha sido cancelado.</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.job.professional.mail, MailType.JOB_CANCELED.subject, content),
            Mail.toNotificationMail(e.job.customer.mail, MailType.JOB_CANCELED.subject, content)
        )
    }
}

class RatingReceivedMail: MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnRatingReceivedEvent
        val content = buildHtml("""
            <p>Hola ${e.rating.userTo},</p>
            <p>${e.rating.userFrom} te ha dejado una reseña!</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.rating.userTo.mail, MailType.RATING_RECEIVED.subject, content)
        )
    }
}