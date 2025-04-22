package quickfix.dto.job.jobOffer

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.SchemaProperty
import quickfix.dto.professional.ProfessionalDTO

@Schema(description = "Oferta enviada por un profesional como respuesta a un jobRequest")
@SchemaProperty(name = "distance", schema = Schema(description = "Distancia en kilómetros a la que se encuentra el profesional del usuario que inició la búsqueda."))
@SchemaProperty(name = "estimatedArriveTime", schema = Schema(description = "Tiempo de llegada estimado en minutos del profesional. Calculado en base a la distancia"))
@SchemaProperty(name = "availability", schema = Schema(description = "Disponibilidad del professional en minutos para iniciar el trabajo."))

data class JobOfferDTO @JsonCreator constructor (

    @JsonProperty("customerId")
    var customerId : Long,

    @JsonProperty("professionId")
    var professionId : Long,

    @JsonProperty("professional")
    var professional: ProfessionalDTO,

    @JsonProperty("price")
    var price: Double,

    @JsonProperty("distance")
    var distance: Double,

    @JsonProperty("estimatedArriveTime")
    var estimatedArriveTime: Int,

    @JsonProperty("availability")
    var availability: Int,

)