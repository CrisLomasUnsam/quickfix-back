package quickfix.dto.job.jobOffer

import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.models.Profession
import java.time.LocalDateTime


data class JobOfferDTO (
    var customer : SeeBasicUserInfoDTO,
    var profession : Profession,
    var professional: SeeBasicUserInfoDTO,
    var price: Double,
    var distance: Double,
    var estimatedArriveTime: Int,
    var jobDuration: Int,
    var jobDurationTimeUnit: String, //Minute, Hour, Day, Week, Month
    var neededDatetime: LocalDateTime,
    var detail: String,
    var instantRequest: Boolean
)