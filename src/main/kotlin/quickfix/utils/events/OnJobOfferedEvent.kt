package quickfix.utils.events

import quickfix.dto.job.jobOffer.JobOfferDTO

class OnJobOfferedEvent(
    val offer: JobOfferDTO
)