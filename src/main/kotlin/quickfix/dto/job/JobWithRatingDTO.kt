package quickfix.dto.job

import quickfix.utils.functions.DateWithDayFormatter
import java.time.LocalDate

interface IJobWithRating {
    fun getId(): Long
    fun getInitDateTime(): LocalDate
    fun getUserName(): String
    fun getUserLastName(): String
    fun getProfessionName(): String
    fun getStatus(): String
    fun getPrice(): Double
    fun getScore(): Int?
}

data class JobWithRatingDTO(
    val id: Long,
    val initDateTime: String,
    val userName: String,
    val userLastName: String,
    val professionName: String,
    val status: String,
    val price: Double,
    val score: Int?,
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
                score = job.getScore()
            )
    }
}