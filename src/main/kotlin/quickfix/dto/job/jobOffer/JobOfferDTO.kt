package quickfix.dto.job.jobOffer

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import quickfix.dto.user.SeeUserBasicInfoDTO
import quickfix.models.Profession
import java.time.LocalDateTime


data class JobOfferDTO @JsonCreator constructor (

    @JsonProperty("customer") var customer : SeeUserBasicInfoDTO,

    @JsonProperty("profession") var profession : Profession,

    @JsonProperty("professional") var professional: SeeUserBasicInfoDTO,

    @JsonProperty("price") var price: Double,

    @JsonProperty("distance") var distance: Double,

    @JsonProperty("estimatedArriveTime") var estimatedArriveTime: Int,

    @JsonProperty("jobDuration") var jobDuration: Int,

    @JsonProperty("jobDurationTimeUnit") var jobDurationTimeUnit: String, //Minute, Hour, Day, Week, Month

    @JsonProperty("neededDatetime") var neededDatetime: LocalDateTime,

    @JsonProperty("detail") var detail: String,

    @JsonProperty("instantRequest") var instantRequest: Boolean

)