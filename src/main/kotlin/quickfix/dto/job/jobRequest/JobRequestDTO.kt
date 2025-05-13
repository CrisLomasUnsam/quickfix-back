package quickfix.dto.job.jobRequest

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException

@Schema(description = "Para traer los jobRequests de Redis")
data class JobRequestRedisDTO @JsonCreator constructor(

    @JsonProperty("customerId")
    var customerId: Long,

    @JsonProperty("professionId")
    var professionId: Long,

    @JsonProperty("detail")
    var detail: String

)

@Schema(description = "Solicitud de un Job por un customer")
data class JobRequestDTO @JsonCreator constructor(

    @JsonProperty("customerId")
    var customerId: Long,

    @JsonProperty("name")
    var customerName: String,

    @JsonProperty("lastName")
    var customerLastName: String,

    @JsonProperty("avatar")
    var customerAvatar: String,

    @JsonProperty("professionId")
    var professionId: Long,

    @JsonProperty("professionName")
    var professionName: String,

    @JsonProperty("detail")
    var detail: String,

    @JsonProperty("rating")
    var rating: Double

) {
    companion object {
        fun toJobRequest(
            jobRequestRedis: JobRequestRedisDTO,
            customer: User,
            professionName: String,
            rating: Double
        ) : JobRequestDTO {
            return JobRequestDTO(
                customerId = jobRequestRedis.customerId,
                customerName = customer.name,
                customerLastName = customer.lastName,
                customerAvatar = "", // si tenés un campo real, ponelo acá
                professionId = jobRequestRedis.professionId,
                professionName = professionName,
                detail = jobRequestRedis.detail,
                rating = rating
            )
        }
    }
}

fun JobRequestDTO.validate() {
    validCustomer(customerId)
    validProfession(professionId)
    validDetail(detail)
}

private fun validCustomer(customerId: Long) { if(customerId < 1) throw BusinessException() }
private fun validProfession(professionId: Long) { if(professionId < 1) throw BusinessException() }
private fun validDetail(detail: String) { if(detail.isBlank()) throw BusinessException() }
