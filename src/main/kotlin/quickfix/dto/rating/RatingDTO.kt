package quickfix.dto.rating

import quickfix.models.Rating

data class RatingDTO(
    val rating: Rating,
)

fun RatingDTO.toRating(): Rating =
    Rating()