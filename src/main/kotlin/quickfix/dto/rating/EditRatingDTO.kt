package quickfix.dto.rating

data class EditRatingDTO(
    val ratingId: Long,
    val score: Int?,
    val comment: String?
)