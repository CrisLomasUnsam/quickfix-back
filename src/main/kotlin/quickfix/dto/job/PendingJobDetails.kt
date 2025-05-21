package quickfix.dto.job

data class PendingJobDetails(
    val distance: Int?,
    val estimatedArrivalMinutes: Int?,
){
    companion object {
        fun toDTO(

        ) : PendingJobDetails {
            return PendingJobDetails(
                distance = null,
                estimatedArrivalMinutes = null
            )
        }
    }
}
