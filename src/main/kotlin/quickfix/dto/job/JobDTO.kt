package quickfix.dto.job

import quickfix.models.Job
import java.time.LocalDate

data class JobDTO(
    val id: Long,
    val date: LocalDate,
    val professionalName: String,
    val profession: String,
    val status: String,
    val price: Double
)

fun toDto(job: Job): JobDTO = JobDTO(
    id               = job.id,
    date             = job.date,
    professionalName = job.professional.name,
    profession       = job.profession.name,
    status           = job.status.name,
    price            = job.price,

)