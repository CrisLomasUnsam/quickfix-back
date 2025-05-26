package quickfix.utils.mailSender

import quickfix.utils.events.OnChangePasswordRequestEvent
import quickfix.utils.events.OnJobStartedEvent
import quickfix.utils.events.OnRatingReceivedEvent
import quickfix.utils.events.OnRegistrationCompletedEvent

object MailFactory {
    fun getStrategyByEvent(event: Any): IMailContent? = when (event) {
        is OnRegistrationCompletedEvent -> RegistrationMail()
        is OnChangePasswordRequestEvent -> PasswordRecoveryMail()
        is OnJobStartedEvent -> JobStartedMail()
        is OnRatingReceivedEvent -> RatingReceivedMail()
        else -> null
    }
}