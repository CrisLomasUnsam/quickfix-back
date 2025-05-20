package quickfix.dto.user

data class SeeUserBasicInfoDTO (
    var id: Long,
    var name: String,
    var lastName: String,
    var avatar: String,
    var averageRating: Double,
    var hasVehicle: Boolean? = null
)