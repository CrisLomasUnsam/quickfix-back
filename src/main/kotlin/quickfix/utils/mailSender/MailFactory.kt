package quickfix.utils.mailSender

import quickfix.utils.events.*

object MailFactory {
    fun getStrategyByEvent(event: Any): IMailContent? = when (event) {
        is OnRegistrationCompletedEvent -> RegistrationMail()
        is OnChangePasswordRequestEvent -> PasswordRecoveryMail()
        is OnRatingReceivedEvent -> RatingReceivedMail()
        is OnRatingEditedEvent -> RatingEditedMail()
        is OnJobNotificationEvent -> JobNotificationMail()
        is OnJobRequestedEvent -> JobRequestedMail()
        is OnJobOfferedEvent -> JobOfferedMail()
        is OnDebtPaidEvent -> DebtPaidMail()
        is OnChangedUserInfoEvent -> UserInfoEditedMail()
        else -> null
    }
}