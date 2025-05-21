package quickfix.dto.job.jobOffer

import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.utils.functions.DatetimeFormatter

data class ProfessionalJobOfferDTO (
    var requestId: String,
    var professionId : Long,
    var customer: SeeBasicUserInfoDTO,
    var price: Double,
    var distance: Double,
    var detail: String,
    var estimatedArriveTime: Int,
    var jobDuration: Int,
    var jobDurationTimeUnit: String,
    var neededDatetime: String,
    var instantRequest: Boolean

    )
{
    companion object {
        fun fromDto(jobOfferDTO: JobOfferDTO): ProfessionalJobOfferDTO {
            val requestId = "${jobOfferDTO.profession.id}_${jobOfferDTO.customer.id}"
            return ProfessionalJobOfferDTO(
                requestId = requestId,
                professionId = jobOfferDTO.profession.id,
                customer = jobOfferDTO.customer,
                price = jobOfferDTO.price,
                distance = jobOfferDTO.distance,
                detail = jobOfferDTO.detail,
                estimatedArriveTime = jobOfferDTO.estimatedArriveTime,
                jobDuration = jobOfferDTO.jobDuration,
                jobDurationTimeUnit = jobOfferDTO.jobDurationTimeUnit,
                neededDatetime = jobOfferDTO.neededDatetime.format(DatetimeFormatter),
                instantRequest = jobOfferDTO.instantRequest
            )
        }
    }
}