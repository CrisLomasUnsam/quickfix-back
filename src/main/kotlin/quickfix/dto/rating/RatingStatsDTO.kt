package quickfix.dto.rating

data class RatingStatsDTO(
    val averageRating: Double,
    val totalRatings: Int,
    val ratingsPerValue: RatingPerValueDTO
)

data class RatingPerValueDTO(
    val amountRating1: Int,
    val amountRating2: Int,
    val amountRating3: Int,
    val amountRating4: Int,
    val amountRating5: Int
)