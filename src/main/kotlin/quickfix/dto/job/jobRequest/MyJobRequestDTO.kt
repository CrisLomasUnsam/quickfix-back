package quickfix.dto.job.jobRequest

import java.time.LocalDateTime

data class MyJobRequestDTO (
    var professionId: Long,
    var jobDetails: String,
    var neededDatetime: LocalDateTime,
    var offersCount: Int
)