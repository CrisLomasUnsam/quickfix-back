package quickfix.dto.professional

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ProfessionalDTO @JsonCreator constructor(
    @JsonProperty("id")             var id: Long,
    @JsonProperty("name")           var name: String,
    @JsonProperty("lastName")       var lastName: String,
    @JsonProperty("verified")       var verified: Boolean,
    @JsonProperty("averageRating")  var averageRating: Double,
    @JsonProperty("hasVehicle")     var hasVehicle: Boolean
){
    companion object {

        fun fromUser(user: quickfix.models.User, averageRating: Double): ProfessionalDTO {
           return ProfessionalDTO(
                id = user.id,
                name = user.name,
                lastName = user.lastName,
                verified = user.verified,
                averageRating = averageRating,
                hasVehicle = user.professionalInfo.hasVehicle
            )
        }
    }
}

