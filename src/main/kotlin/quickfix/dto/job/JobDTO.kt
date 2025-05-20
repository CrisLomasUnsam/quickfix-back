package quickfix.dto.job

import quickfix.models.Job
import quickfix.models.User
import java.time.LocalDateTime

data class JobDTO(
    val id: Long,
    val userName: String,
    val userLastName: String,
    val userAverageRating : Double,
    val userIsVerified: Boolean,
    val profession: String,
    val status: String,
    val price: Double,
    val initDateTime: LocalDateTime,
    val duration: Int,
    val durationUnit: String
) {
    companion object {
        fun toDto(job: Job, requesterIsCustomer: Boolean): JobDTO {
            val user : User = if(requesterIsCustomer) job.professional else job.customer
            return JobDTO(
                id = job.id,
                userName = user.name,
                userLastName = user.lastName,
                userAverageRating = user.averageRating,
                userIsVerified = user.verified,
                profession = job.profession.name,
                status = job.status.name,
                price = job.price,
                initDateTime = job.initDateTime,
                duration = job.duration,
                durationUnit = job.durationUnit
            )
        }
    }
}