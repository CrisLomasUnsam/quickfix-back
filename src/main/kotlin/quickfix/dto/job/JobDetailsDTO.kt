package quickfix.dto.job

import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.models.Job
import quickfix.utils.enums.JobStatus
import quickfix.utils.exceptions.JobException
import quickfix.utils.functions.stringifyDateTime

data class JobDetailsDTO (
    val id : Long,
    val professionId: Long,
    val detail: String,
    val price: Double,
    val rated: Boolean,
    val dateTime: String,
    val userInfo: SeeBasicUserInfoDTO,
    val status: JobStatus,
    val pendingJobDetails: PendingJobDetails?
) {
    companion object {
        fun toDTO(
            currentUserId: Long,
            job: Job,
            seeCustomerInfo: Boolean,
            isRated: Boolean,
            totalRatings: Int
        ): JobDetailsDTO {
            val user = when (currentUserId) {
                job.customer.id -> job.professional
                job.professional.id -> job.customer
                else -> throw JobException("No existe el Job")
            }
            return JobDetailsDTO(
                id = job.id,
                professionId = job.profession.id,
                detail = job.detail,
                price = job.price,
                rated = isRated,
                dateTime = stringifyDateTime(job.initDateTime),
                userInfo = SeeBasicUserInfoDTO.toDTO(user, seeCustomerInfo, totalRatings),
                status = job.status,
                pendingJobDetails = null
            )
        }
    }
}

data class PendingJobDetails(
    val distance: Int?,
    val estimatedArrivalMinutes: Int?,
)