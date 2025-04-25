package quickfix.dto.job.jobOffer

import quickfix.dto.professional.ProfessionalDTO

data class CancelJobOfferDTO (
    val professionId: Long,
    val customerId: Long,
    val professionalId: Long
)

