package quickfix.dto.job

data class CancelOfferJobDTO (
    val professionId: Long,
    val customerId: Long,
    val professionalId: Long
)