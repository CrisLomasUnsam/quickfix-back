package quickfix.dto.rating

import com.fasterxml.jackson.annotation.JsonProperty
import quickfix.models.Rating
import quickfix.utils.functions.toYearMonthString

data class RatingDTO(

    @field:JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val jobId: Long,
    val score: Int,
    val comment: String,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val yearAndMonth: String? = null
)

fun Rating.toDTO(): RatingDTO {

    return RatingDTO(
        jobId = this.job.id,
        score = this.score,
        comment = this.comment,
        yearAndMonth = yearAndMonth.toYearMonthString(),
    )


}