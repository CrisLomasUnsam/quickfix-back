package quickfix.dto.job

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.models.Customer
import quickfix.models.JobRequest
import quickfix.models.Profession

@Schema(description = "Solicitud de un Job por un customer, consumido por observers")
data class JobRequestDTO(
    var customer: Customer,
    var profession: Profession,
    var detail: String
)

fun JobRequestDTO.toJobRequest(): JobRequest =
    JobRequest(
        customer = this.customer,
        profession = this.profession,
        detail = this.detail
    ).apply {
        this.validate()
    }
