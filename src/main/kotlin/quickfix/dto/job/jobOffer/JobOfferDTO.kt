package quickfix.dto.job.jobOffer

import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.user.SeeBasicUserInfoDTO


data class JobOfferDTO (
    var professional: SeeBasicUserInfoDTO,
    var price: Double,
    var distance: Double,
    var estimatedArriveTime: Int,
    var duration: Int,
    var durationUnit: String, //Minute, Hour, Day, Week, Month
    var request: JobRequestDTO
)