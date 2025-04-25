package quickfix.dto.job.jobOffer

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


data class CreateJobOfferDTO @JsonCreator constructor (

    @JsonProperty
    var customerId : Long,

    @JsonProperty
    var professionId : Long,

    @JsonProperty
    var professionalId: Long,

    @JsonProperty
    var price: Double,

    @JsonProperty
    var distance: Double,

    @JsonProperty
    var estimatedArriveTime: Int,

    @JsonProperty
    var availability: Int

    )