package quickfix.dto.job

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.SchemaProperty

@Schema(description = "Oferta enviada por un profesional como respuesta a un jobRequest")
@SchemaProperty(name = "distance", schema = Schema(description = "Distancia en kilómetros a la que se encuentra el profesional del usuario que inició la búsqueda."))
@SchemaProperty(name = "estimatedArriveTime", schema = Schema(description = "Tiempo de llegada estimado en minutos del profesional. Calculado en base a la distancia"))
@SchemaProperty(name = "availability", schema = Schema(description = "Disponibilidad del professional en minutos para iniciar el trabajo."))

data class JobOfferDTO (
    val professionalId : Long,
    val customerId : Long,
    val profession : String,
    val name: String,
    val lastName: String,
    val avatar : String,
    val verified: Boolean,
    val price: Double,
    val averageRating: Double,
    val distance: Double,
    val estimatedArriveTime: Int,
    val availability: Int,
    val hasVehicle: Boolean
)