package quickfix.dto.job.jobRequest

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import quickfix.utils.exceptions.CustomerException
import quickfix.utils.exceptions.DetailException
import quickfix.utils.exceptions.ProfessionException

@Schema(description = "Solicitud de un Job por un customer")
data class JobRequestDTO @JsonCreator constructor(

    @JsonProperty("customerId")
    var customerId: Long,

    @JsonProperty("name")
    var name: String,

    @JsonProperty("lastName")
    var lastName: String,

    @JsonProperty("avatar")
    var avatar: String,

    @JsonProperty("professionId")
    var professionId: Long,

    @JsonProperty("professionName")
    var professionName: String,

    @JsonProperty("detail")
    var detail: String,

    @JsonProperty("rating")
    var rating: Double

)

fun JobRequestDTO.validate() {
    validCustomer(customerId)
    validProfession(professionId)
    validDetail(detail)
}

private fun validCustomer(customerId: Long) { if(customerId < 1) throw CustomerException("No existe un customer con ese ID") }
private fun validProfession(professionId: Long) { if(professionId < 1) throw ProfessionException("El ID de la profesión no matchea con una profesión en la BBDD") }
private fun validDetail(detail: String) { if(detail.isBlank()) throw DetailException("EL detalle no puede estar vacío") }
