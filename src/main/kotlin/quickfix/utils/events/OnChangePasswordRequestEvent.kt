package quickfix.utils.events

import quickfix.models.User

class OnChangePasswordRequestEvent(
    val user: User,
    val recoveryURL: String,
)