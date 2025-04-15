package quickfix.dto.rating

import quickfix.utils.datifyString
import java.time.YearMonth

data class EditRating(
    val score: Int?,
    val yearAndMonth: YearMonth?,
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
        yearAndMonth = yearAndMonth
            ?.takeIf { it.isNotBlank() }
            ?.let { datifyString(it) },
        comment = comment
    )
}