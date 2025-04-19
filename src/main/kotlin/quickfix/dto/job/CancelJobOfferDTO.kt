package quickfix.dto.job

data class CancelJobOfferDTO (
    val professionId: Long,
    val customerId: Long,
    val professionalId: Long
)