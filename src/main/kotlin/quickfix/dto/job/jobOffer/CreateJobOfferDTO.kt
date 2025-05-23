package quickfix.dto.job.jobOffer

import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.models.User

data class CreateJobOfferDTO(
    val requestId: String,
    val duration: Int,
    val durationUnit: String,
    val price: Double
) {
    companion object {
        fun toJobOffer(offer: CreateJobOfferDTO, jobRequest: JobRequestDTO, professional: User) : JobOfferDTO {
            return JobOfferDTO(
                professional = SeeBasicUserInfoDTO.toDto(professional, false),
                price = offer.price,
                distance = 10.1, //TODO: Crear servicio o singleton que calcule estos datos
                estimatedArriveTime = 20, //TODO: Crear servicio o singleton que calcule estos datos
                duration = offer.duration,
                durationUnit = offer.durationUnit,
                request = jobRequest
            )
        }

    }
}