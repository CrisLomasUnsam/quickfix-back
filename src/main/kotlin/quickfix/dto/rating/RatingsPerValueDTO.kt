package quickfix.dto.rating

import quickfix.models.RatingStatsProjection

data class RatingsPerValueDTO(
    val amountRating1: Int,
    val amountRating2: Int,
    val amountRating3: Int,
    val amountRating4: Int,
    val amountRating5: Int
){
    companion object {
        fun from(p: RatingStatsProjection) = RatingsPerValueDTO(
            amountRating1 = p.getAmountRating1(),
            amountRating2 = p.getAmountRating2(),
            amountRating3 = p.getAmountRating3(),
            amountRating4 = p.getAmountRating4(),
            amountRating5 = p.getAmountRating5()
        )
    }
}