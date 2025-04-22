package quickfix.dto.professional

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ProfessionalDTO @JsonCreator constructor(
    @JsonProperty("id")   var id: Long,
    @JsonProperty("name")   var name: String,
    @JsonProperty("lastName")   var lastName: String,
    @JsonProperty("avatar")   var avatar: String,
    @JsonProperty("verified")   var verified: String,
    @JsonProperty("averageRating")   var averageRating: Double,
    @JsonProperty("hasVehicle")   var hasVehicle: Boolean
)
