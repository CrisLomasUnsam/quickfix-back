package quickfix.dto.job.jobOffer


data class CreateJobOfferDTO(
    var customerId : Long,
    var professionId : Long,
    var professionalId: Long,
    var price: Double,
    var distance: Double,
    var estimatedArriveTime: Int,
    var availability: Int,
    )