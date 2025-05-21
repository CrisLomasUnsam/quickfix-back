package quickfix.dto.job.jobRequest

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.utils.functions.DatetimeFormatter

@Schema(description = "JobRequest vista desde un customer (en su sección Mis Solicitudes)")
data class CustomerJobRequestDTO (
    var professionId: Long,
    var jobDetails: String,
    var neededDatetime: String,
    var instantRequest: Boolean,
    var offersCount: Int
) {
    companion object {
        fun fromJobRequest(jobRequest: JobRequestDTO, offersCount: Int): CustomerJobRequestDTO {
            return CustomerJobRequestDTO(
                professionId = jobRequest.professionId,
                jobDetails = jobRequest.detail,
                neededDatetime = jobRequest.neededDatetime.format(DatetimeFormatter),
                instantRequest = jobRequest.instantRequest,
                offersCount = offersCount
            )
        }
    }
}