package quickfix.dto.job.jobOffer


data class AcceptJobOfferDTO (

    val professionalId : Long,
    val customerId: Long ,
    val professionId: Long,
    val availability: Int,
    val price: Double

)