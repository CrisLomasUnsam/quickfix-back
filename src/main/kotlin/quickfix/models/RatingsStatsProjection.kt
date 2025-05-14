package quickfix.models

interface RatingStatsProjection{

    fun getTotal(): Int
    fun getAvg(): Double
    fun getAmountRating1(): Int
    fun getAmountRating2(): Int
    fun getAmountRating3(): Int
    fun getAmountRating4(): Int
    fun getAmountRating5(): Int

}