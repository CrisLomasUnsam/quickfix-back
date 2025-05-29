package quickfix.utils.events

import quickfix.dto.job.jobRequest.JobRequestDTO

class OnJobRequestedEvent(
    val request: JobRequestDTO
)