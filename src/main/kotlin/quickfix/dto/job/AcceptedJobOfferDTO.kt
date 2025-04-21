package quickfix.dto.job


data class AcceptedJobOfferDTO (
    val professionalId : Long,
    val customerId: Long ,
    val profession: String,
    val availability: Int,
    val price: Double
)