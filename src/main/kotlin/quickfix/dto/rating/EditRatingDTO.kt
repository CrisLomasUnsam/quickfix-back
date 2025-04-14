package quickfix.dto.rating

import java.time.LocalDate

data class EditRatingDTO(
    val score: Int?,
    val yearAndMonth: LocalDate?,
    val comment: String?
)
