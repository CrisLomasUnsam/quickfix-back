package quickfix.dto.rating

import quickfix.models.RatingStatsProjection

data class RatingsStatsDTO(
    val averageRating: Double,
    val totalRatings: Int,
    val ratingsPerValue: RatingsPerValueDTO
){
    companion object {
        fun from(p: RatingStatsProjection) = RatingsStatsDTO(
            averageRating   = p.getAvg(),
            totalRatings    = p.getTotal(),
            ratingsPerValue = RatingsPerValueDTO.from(p)
        )
    }
}