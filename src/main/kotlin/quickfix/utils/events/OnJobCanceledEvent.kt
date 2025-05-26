package quickfix.utils.events

import quickfix.models.Job

class OnJobCanceledEvent(
    val job: Job
)