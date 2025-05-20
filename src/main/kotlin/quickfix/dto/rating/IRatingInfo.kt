package quickfix.dto.rating

interface IRatingInfo {
    fun getAmount(): Int
    fun getTotal(): Double
}

data class RatingInfoDTO(
    val amount: Int,
    val total: Double
){
    companion object {
        fun toDTO(ratingInfo: IRatingInfo): RatingInfoDTO {
            return RatingInfoDTO(
                amount =  ratingInfo.getAmount(),
                total = ratingInfo.getTotal()
            )
        }
    }
}