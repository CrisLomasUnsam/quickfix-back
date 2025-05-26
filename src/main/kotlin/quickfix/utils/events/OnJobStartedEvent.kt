package quickfix.utils.events

import quickfix.models.Job

class OnJobStartedEvent(
    val job: Job
)