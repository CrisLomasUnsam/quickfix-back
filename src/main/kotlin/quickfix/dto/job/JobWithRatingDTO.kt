package quickfix.dto.job

import quickfix.utils.enums.JobStatus
import quickfix.utils.functions.DateWithDayFormatter
import quickfix.utils.functions.getAvatarUrl
import java.time.LocalDate

interface IJobWithRating {
    fun getId(): Long
    fun getInitDateTime(): LocalDate
    fun getUserName(): String
    fun getUserLastName(): String
    fun getProfessionName(): String
    fun getStatus(): JobStatus
    fun getPrice(): Double
    fun getScore(): Int?
    fun getUserId(): Long
}

data class JobWithRatingDTO(
    val id: Long,
    val initDateTime: String,
    val userName: String,
    val userLastName: String,
    val professionName: String,
    val status: JobStatus,
    val price: Double,
    val score: Int?,
    val avatar: String
) {
    companion object {
        fun fromProjection(job : IJobWithRating) : JobWithRatingDTO =
            JobWithRatingDTO(
                id = job.getId(),
                initDateTime = job.getInitDateTime().format(DateWithDayFormatter),
                userName = job.getUserName(),
                userLastName = job.getUserLastName(),
                professionName = job.getProfessionName(),
                status = job.getStatus(),
                price = job.getPrice(),
                score = job.getScore(),
                avatar = getAvatarUrl(job.getUserId())
            )
    }
}