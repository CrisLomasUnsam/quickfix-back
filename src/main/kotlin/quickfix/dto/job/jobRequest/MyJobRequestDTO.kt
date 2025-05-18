package quickfix.dto.job.jobRequest

import quickfix.utils.functions.DatetimeFormatter

data class MyJobRequestDTO (
    var professionId: Long,
    var jobDetails: String,
    var neededDatetime: String,
    var instantRequest: Boolean,
    var offersCount: Int
) {
    companion object {
        fun fromJobRequest(jobRequest: JobRequestDTO, offersCount: Int): MyJobRequestDTO {
            return MyJobRequestDTO(
                professionId = jobRequest.professionId,
                jobDetails = jobRequest.detail,
                neededDatetime = jobRequest.neededDatetime.format(DatetimeFormatter),
                instantRequest = jobRequest.instantRequest,
                offersCount = offersCount
            )
        }
    }
}

fun MyJobRequestDTO.fromJobRequest(jobRequest: JobRequestDTO){

}