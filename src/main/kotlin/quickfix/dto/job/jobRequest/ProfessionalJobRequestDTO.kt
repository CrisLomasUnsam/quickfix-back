package quickfix.dto.job.jobRequest

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.utils.functions.DatetimeFormatter
import quickfix.utils.jobs.getJobRequestKey

@Schema(description = "JobRequest vista desde el home del profesional")
data class ProfessionalJobRequestDTO (
    var requestId: String,
    var customer: SeeBasicUserInfoDTO,
    var professionId: Long,
    var detail: String,
    var neededDatetime: String,
    var instantRequest: Boolean = false,
    var distance: Double
) {
    companion object {
        fun fromJobRequest(jobRequest: JobRequestDTO, distance: Double = 0.0): ProfessionalJobRequestDTO {
            return ProfessionalJobRequestDTO(
                requestId = getJobRequestKey(jobRequest.professionId, jobRequest.customer.id),
                customer = jobRequest.customer,
                professionId = jobRequest.professionId,
                detail = jobRequest.detail,
                neededDatetime = jobRequest.neededDatetime.format(DatetimeFormatter),
                instantRequest = jobRequest.instantRequest,
                distance = distance
            )
        }
    }
}

