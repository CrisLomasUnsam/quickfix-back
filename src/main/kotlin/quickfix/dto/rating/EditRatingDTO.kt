package quickfix.dto.rating

import quickfix.utils.datifyString
import java.time.LocalDate

data class EditRating(
    val score: Int?,
    val yearAndMonth: LocalDate?,
    val comment: String?
)

data class EditRatingDTO(
    val score: Int?,
    val yearAndMonth: String?,
    val comment: String?
)

fun EditRatingDTO.fromDTO(): EditRating {
    return EditRating(
        score = score,
        yearAndMonth = this.yearAndMonth?.let { datifyString(it) },
        comment = comment
    )
}