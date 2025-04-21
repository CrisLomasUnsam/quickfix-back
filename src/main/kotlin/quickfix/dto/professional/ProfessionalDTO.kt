package quickfix.dto.professional

data class ProfessionalDTO(
    val id: Long,
    val name: String,
    val lastName: String,
    val avatar: String,
    val verified: String,
    val averageRating: Double,
    val hasVehicle: Boolean
)
