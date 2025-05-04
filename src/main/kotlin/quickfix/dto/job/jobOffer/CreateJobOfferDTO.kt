package quickfix.dto.job.jobOffer

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


data class CreateJobOfferDTO @JsonCreator constructor (

    @JsonProperty("customerId")
    var customerId : Long,

    @JsonProperty("professionId")
    var professionId : Long,

    @JsonProperty("professionalId")
    var professionalId: Long,

    @JsonProperty("price")
    var price: Double,

    @JsonProperty("distance")
    var distance: Double,

    @JsonProperty("estimatedArriveTime")
    var estimatedArriveTime: Int,

    @JsonProperty("availability")
    var availability: Int

    )