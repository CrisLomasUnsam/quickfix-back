package quickfix.dto.job

import quickfix.models.Address

data class JobSearchRequestDTO(
    var job_id : Long,
    var customer_id : Long,
    var detail: String,
    var address: Address
)
