package quickfix.dto.user

import quickfix.dto.rating.RatingPerValueDTO
import quickfix.dto.rating.RatingStatsDTO
import quickfix.utils.functions.getAvatarUrl

interface  ISeeUserProfile{

    fun getId(): Long
    fun getName(): String
    fun getLastName(): String
    fun getVerified(): Boolean
    fun getTotalJobsFinished(): Int
    fun getAverageRating(): Double
    fun getTotalRatings(): Int
    fun getAmountRating1(): Int
    fun getAmountRating2(): Int
    fun getAmountRating3(): Int
    fun getAmountRating4(): Int
    fun getAmountRating5(): Int

}

data class SeeUserProfileDTO(
    val id: Long,
    val name: String,
    val lastName: String,
    val avatar: String,
    val verified: Boolean,
    val totalJobsFinished: Int,
    val ratingStats : RatingStatsDTO
) {
    companion object {
        fun fromProjection(seeUserProfileInfo : ISeeUserProfile) : SeeUserProfileDTO {
            val ratingsPerValue = RatingPerValueDTO(
                amountRating1 = seeUserProfileInfo.getAmountRating1(),
                amountRating2 = seeUserProfileInfo.getAmountRating2(),
                amountRating3 = seeUserProfileInfo.getAmountRating3(),
                amountRating4 = seeUserProfileInfo.getAmountRating4(),
                amountRating5 = seeUserProfileInfo.getAmountRating5(),
            )
            val ratingStats = RatingStatsDTO(
                averageRating = seeUserProfileInfo.getAverageRating(),
                totalRatings = seeUserProfileInfo.getTotalRatings(),
                ratingsPerValue = ratingsPerValue
            )
            return SeeUserProfileDTO(
                id = seeUserProfileInfo.getId(),
                name = seeUserProfileInfo.getName(),
                lastName = seeUserProfileInfo.getLastName(),
                avatar = getAvatarUrl(seeUserProfileInfo.getId()),
                verified = seeUserProfileInfo.getVerified(),
                totalJobsFinished = seeUserProfileInfo.getTotalJobsFinished(),
                ratingStats = ratingStats
            )
        }
    }
}