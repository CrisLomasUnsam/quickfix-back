package quickfix.dto.professional

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Base64
data class ProfessionalDTO @JsonCreator constructor(
    @JsonProperty("id")             var id: Long,
    @JsonProperty("name")           var name: String,
    @JsonProperty("lastName")       var lastName: String,
    @JsonProperty("avatar")         var avatar: String,
    @JsonProperty("verified")       var verified: Boolean,
    @JsonProperty("averageRating")  var averageRating: Double,
    @JsonProperty("hasVehicle")     var hasVehicle: Boolean
){
    companion object {

        fun fromUser(user: quickfix.models.User, averageRating: Double): ProfessionalDTO {
            val avatarBase64 = user.avatar.let { Base64.getEncoder().encodeToString(it) }
                ?: ""
           return ProfessionalDTO(
                id = user.id,
                name = user.name,
                lastName = user.lastName,
                avatar = avatarBase64,
                verified = user.verified,
                averageRating = averageRating,
                hasVehicle = user.professionalInfo.hasVehicle
            )
        }
    }
}

