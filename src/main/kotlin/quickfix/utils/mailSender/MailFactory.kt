package quickfix.utils.mailSender

import quickfix.utils.events.OnChangePasswordRequestEvent
import quickfix.utils.events.OnRegistrationCompletedEvent

object MailFactory {
    fun getStrategyByEvent(event: Any): IMailContent? = when (event) {
        is OnRegistrationCompletedEvent -> RegistrationMail()
        is OnChangePasswordRequestEvent -> PasswordRecovery()
        else -> null
    }
}