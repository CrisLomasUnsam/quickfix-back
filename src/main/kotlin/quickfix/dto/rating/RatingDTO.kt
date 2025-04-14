package quickfix.dto.rating

import quickfix.models.Rating
import java.time.LocalDate

data class RatingDTO(
    val userFromId: Long,
    val userToId: Long,
    val jobId: Long,
    val score: Int,
    val yearAndMonth: LocalDate,
    val comment: String
)

fun RatingDTO.toRating(): Rating =
    Rating()