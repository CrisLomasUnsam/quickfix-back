package quickfix.dto.job

import quickfix.dto.job.jobRequest.PendingJobDetails
import quickfix.dto.user.UserInfo
import quickfix.models.Job
import quickfix.utils.enums.JobStatus
import quickfix.utils.exceptions.JobException
import quickfix.utils.functions.stringifyDateWithHours

data class JobDetails (
    val id : Long,
    val professionName: String,
    val description: String,
    val price: Double,
    val rated: Boolean,
    val date: String,
    val userInfo: UserInfo,
    val status: JobStatus,
    val pendingJobDetails: PendingJobDetails?
) {
    companion object {
        fun toDTO(
            currentCustomerId: Long,
            job: Job,

        ): JobDetails {
            val user = when (currentCustomerId) {
                job.customer.id -> job.professional
                job.professional.id -> job.customer
                else -> throw JobException("No existe el Job")
            }
            return JobDetails(
                id = job.id,
                professionName = job.profession.name,
                description = job.description,
                price = job.price,
                rated = false ,
                date = stringifyDateWithHours(job.initDateTime),
                userInfo = UserInfo.toDTO(user),
                status = job.status,
                pendingJobDetails = PendingJobDetails.toDTO()
            )
        }
    }
}
