package quickfix.dto.job

import quickfix.utils.enums.JobStatus
import java.time.LocalDateTime

data class JobDetailsDTO(
    val professionalName : String,
    val verified : Boolean,
    val profession: String,
    val details: String,
    val initDateTime: LocalDateTime,
    val price: Double,
    val status: JobStatus
)
