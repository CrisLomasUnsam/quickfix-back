package quickfix.dto.job.jobOffer

import quickfix.dto.user.SeeUserBasicInfoDTO

data class ProfessionalJobOfferDTO (
    var professionalId : Long,
    var professionId : Long,
    var customer: SeeUserBasicInfoDTO,
    var price: Double,
    var distance: Double,
    var estimatedArriveTime: Int,
    var jobDuration: Int,
    var jobDurationTimeUnit: String,

    )
{
    companion object {
        fun fromDto(jobOfferDTO: JobOfferDTO): ProfessionalJobOfferDTO {
            return ProfessionalJobOfferDTO(
                professionalId = jobOfferDTO.professional.id,
                professionId = jobOfferDTO.profession.id,
                customer = jobOfferDTO.customer,
                price = jobOfferDTO.price,
                distance = jobOfferDTO.distance,
                estimatedArriveTime = jobOfferDTO.estimatedArriveTime,
                jobDuration = jobOfferDTO.jobDuration,
                jobDurationTimeUnit = jobOfferDTO.jobDurationTimeUnit
            )
        }
    }
}