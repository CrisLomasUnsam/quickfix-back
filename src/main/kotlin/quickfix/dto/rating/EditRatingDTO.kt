package quickfix.dto.rating

data class EditRating(
    val score: Int?,
    val comment: String?
)

data class EditRatingDTO(
    val score: Int?,
    val comment: String?
)

fun EditRatingDTO.fromDTO(): EditRating {
    return EditRating(
        score = score,
        comment = comment
    )
}