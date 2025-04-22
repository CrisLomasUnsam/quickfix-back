package quickfix.dto.job.jobRequest

data class CancelJobRequestDTO(
    val customerId: Long,
    val professionId: Long
)
