package quickfix.dto.job

import quickfix.models.Job
import java.time.LocalDate

data class JobDTO(
    val id: Long,
    val professionalId: Long,
    val customerId: Long,
    val date: LocalDate,
    val professionName: String,
    val status: String,
    val price: Double
)

fun toDto(job: Job): JobDTO = JobDTO(
    id             = job.id,
    professionalId = job.professional.id,
    customerId     = job.customer.id,
    date           = job.date,
    professionName = job.profession.name,
    status         = job.status.name,
    price          = job.price
)