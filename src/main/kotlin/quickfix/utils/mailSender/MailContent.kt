package quickfix.utils.mailSender

import quickfix.utils.enums.MailType
import quickfix.utils.events.*

class RegistrationMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnRegistrationCompletedEvent
        val content = buildHtml("""
            <p>Hola ${e.user.name},</p>
            <p>Gracias por registrarte. Confirmá tu cuenta aquí:</p>
            
            <p><a href="${e.confirmationURL}">${e.confirmationURL}</a></p>
            
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
            
            <p><a href="${e.recoveryURL}">${e.recoveryURL}</a></p>            
            
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

class JobDoneMail : MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnJobDoneEvent
        val content = buildHtml("""
            <p>El trabajo de ${e.job.profession.name} ha finalizado.</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.job.professional.mail, MailType.JOB_DONE.subject, content),
            Mail.toNotificationMail(e.job.customer.mail, MailType.JOB_DONE.subject, content)
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
            <p>Hola ${e.rating.userTo.name},</p>
            <p>${e.rating.userFrom.name} te ha dejado una calificación!</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.rating.userTo.mail, MailType.RATING_RECEIVED.subject, content)
        )
    }
}

class RatingEditedMail: MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnRatingEditedEvent
        val content = buildHtml("""
            <p>Hola ${e.rating.userTo.name},</p>
            <p>${e.rating.userFrom.name} ha modificado una calificación realizada el ${e.rating.yearAndMonth}</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.rating.userTo.mail, MailType.RATING_EDITED.subject, content)
        )
    }
}

class DebtPaidMail: MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnDebtPaidEvent
        val content = buildHtml("""
            <p>Hola ${e.name},</p>
            <p>Hemos registrado el pago de tu deuda exitosamente!</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.mail, MailType.DEBT_PAID.subject, content)
        )
    }
}

class UserInfoEditedMail: MailTemplate(), IMailContent {
    override fun generateMail(event: Any): List<Mail> {
        val e = event as OnChangedUserInfoEvent
        val content = buildHtml("""
            <p>Sus datos han sido modificados exitosamente.</p>
        """.trimIndent())
        return listOf(
            Mail.toNotificationMail(e.mail, MailType.INFO_EDITED.subject, content)
        )
    }
}

//class ChatSentMail : MailTemplate(), IMailContent {
//    override fun generateMail(event: Any): List<Mail> {
//        val e = event as OnChatSentEvent
//
//        if (e.isCustomer) {
//            val content = buildHtml("""
//            <p>Te llegó un mensaje de ${e.} ha sido cancelado.</p>
//        """.trimIndent())
//            return listOf(
//                Mail.toNotificationMail(e.job.professional.mail, MailType.JOB_CANCELED.subject, content),
//            )
//        } else {
//            val content = buildHtml("""
//            <p>El trabajo de ${e.job.profession.name} ha sido cancelado.</p>
//        """.trimIndent())
//            return listOf(
//                Mail.toNotificationMail(e.job.professional.mail, MailType.JOB_CANCELED.subject, content),
//            )
//        }
//    }
//}