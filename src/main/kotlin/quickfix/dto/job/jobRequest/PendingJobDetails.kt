package quickfix.dto.job.jobRequest

data class PendingJobDetails (
    val distance: Int?,
    val estimatedArrivalMinutes: Int?,
    val estimatedInitDate: String?,
    val estimatedInitTime: String?,
){
    companion object {
        fun toDTO(

        ) : PendingJobDetails {
            return PendingJobDetails(
                distance = null,
                estimatedArrivalMinutes = null,
                estimatedInitDate = null,
                estimatedInitTime = null
            )
        }
    }
}
