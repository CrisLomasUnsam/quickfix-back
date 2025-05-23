package quickfix.dto.job.jobOffer

import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.utils.jobs.getJobRequestKey

data class ProfessionalJobOfferDTO (
    var requestId: String,
    var price: Double,
    var distance: Double,
    var estimatedArriveTime: Int,
    var duration: Int,
    var durationUnit: String,
    var request: JobRequestDTO
    )
{
    companion object {
        fun fromDto(jobOfferDTO: JobOfferDTO): ProfessionalJobOfferDTO {
            val request = jobOfferDTO.request
            val professionId = request.professionId
            return ProfessionalJobOfferDTO(
                requestId = getJobRequestKey(professionId, request.customer.id),
                price = jobOfferDTO.price,
                distance = jobOfferDTO.distance,
                estimatedArriveTime = jobOfferDTO.estimatedArriveTime,
                duration = jobOfferDTO.duration,
                durationUnit = jobOfferDTO.durationUnit,
                request = request,
            )
        }
    }
}