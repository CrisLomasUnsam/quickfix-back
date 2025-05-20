package quickfix.dto.job

import quickfix.dto.job.jobRequest.PendingJobDetails
import quickfix.dto.user.UserInfoDTO
import quickfix.models.Job
import quickfix.utils.enums.JobStatus
import quickfix.utils.functions.stringifyDateWithHours

data class JobDetailsDTO (
    val id : Long,
    val professionName: String,
    val description: String,
    val price: Double,
    val rated: Boolean,
    val date: String,
    val userInfo: UserInfoDTO,
    val status: JobStatus,
    val pendingJobDetails: PendingJobDetails?
) {
    companion object {
        fun toDTO(
            currentCustomerId: Long,
            job: Job,

        ): JobDetailsDTO {
            val user = when (currentCustomerId) {
                job.customer.id -> job.customer
                job.professional.id -> job.professional
                else -> throw IllegalArgumentException("Invalid job id")
            }
            return JobDetailsDTO(
                id = job.id,
                professionName = job.profession.name,
                description = job.description,
                price = job.price,
                rated = false ,
                date = stringifyDateWithHours(job.initDateTime),
                userInfo = UserInfoDTO.toDTO(user),
                status = job.status,
                pendingJobDetails = PendingJobDetails.toDTO()
            )
        }
    }
}
