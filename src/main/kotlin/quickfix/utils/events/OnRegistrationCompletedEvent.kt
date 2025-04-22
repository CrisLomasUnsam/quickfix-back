package quickfix.utils.events

import quickfix.models.User

class OnRegistrationCompletedEvent(
    val user: User,
    val confirmationURL: String
)