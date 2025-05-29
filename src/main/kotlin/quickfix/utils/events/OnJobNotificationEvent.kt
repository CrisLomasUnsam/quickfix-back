package quickfix.utils.events

import quickfix.models.Job
import quickfix.utils.enums.MailType

class OnJobNotificationEvent(
    val job: Job,
    val type: MailType
)