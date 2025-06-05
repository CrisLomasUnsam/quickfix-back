package quickfix.dto.job.jobOffer

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.SchemaProperty
import quickfix.dto.user.SeeBasicUserInfoDTO

@Schema(description = "Oferta enviada por un profesional como respuesta a un jobRequest")
@SchemaProperty(name = "distance", schema = Schema(description = "Distancia en kilómetros a la que se encuentra el profesional del usuario que inició la búsqueda."))
@SchemaProperty(name = "estimatedArriveTime", schema = Schema(description = "Tiempo de llegada estimado en minutos del profesional. Calculado en base a la distancia"))
@SchemaProperty(name = "availability", schema = Schema(description = "Disponibilidad del professional en minutos para iniciar el trabajo."))

data class CustomerJobOfferDTO (
    var professional: SeeBasicUserInfoDTO,
    var price: Double,
    var distance: Double,
    var duration: Int,
    var durationUnit: String,

    ) {
    companion object {
        fun fromDto(jobOfferDTO: JobOfferDTO): CustomerJobOfferDTO {
            return CustomerJobOfferDTO(
                professional = jobOfferDTO.professional,
                price = jobOfferDTO.price,
                distance = jobOfferDTO.distance,
                duration = jobOfferDTO.duration,
                durationUnit = jobOfferDTO.durationUnit
            )
        }
    }
}