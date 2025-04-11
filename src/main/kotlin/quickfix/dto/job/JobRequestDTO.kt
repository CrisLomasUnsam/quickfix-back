package quickfix.dto.job

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.models.Address
import quickfix.models.JobRequest
import quickfix.models.Profession
import quickfix.models.User

@Schema(description = "Solicitud de un Job por un customer, consumido por observers")
data class JobRequestDTO(
    var customer: User,
    var profession: Profession,
    var detail: String,
    var address: Address
)

fun JobRequestDTO.toJobRequest(): JobRequest =
    JobRequest(
        customer = this.customer,
        profession = this.profession,
        detail = this.detail,
        address = this.address
    ).apply {
        this.validate()
    }
