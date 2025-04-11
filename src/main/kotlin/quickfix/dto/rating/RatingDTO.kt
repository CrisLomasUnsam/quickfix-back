package quickfix.dto.rating

import quickfix.dto.user.UserDTO
import quickfix.models.Rating

data class RatingDTO(
    val userFromId: Long,
    val userToId: Long,
    val jobId: Long,
    val score: Int,
    val comment: String
)

fun RatingDTO.toRating(): Rating =
    Rating()