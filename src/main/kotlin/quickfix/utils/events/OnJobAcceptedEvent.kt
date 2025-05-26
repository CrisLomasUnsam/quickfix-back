package quickfix.utils.events

import quickfix.models.Job

class OnJobAcceptedEvent(
    val job: Job
)