package quickfix.models

import quickfix.dto.rating.RatingsPerValueDTO

interface  UserProfileProjectionDTO{

    fun getId(): Long
    fun getName(): String
    fun getLastName(): String
    fun getVerified(): Boolean
    fun getTotalJobsFinished(): Int
    fun getAverageRating(): Double?
    fun getTotalRatings(): Int
    fun getAmountRating1(): Int
    fun getAmountRating2(): Int
    fun getAmountRating3(): Int
    fun getAmountRating4(): Int
    fun getAmountRating5(): Int

}