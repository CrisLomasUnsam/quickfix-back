package quickfix.dto.job

import java.time.LocalDate

interface JobProjection {
    fun getId(): Long
    fun getDate(): LocalDate
    fun getUserName(): String
    fun getUserLastName(): String
    fun getProfession(): String
    fun getStatus(): String
    fun getPrice(): Double
    fun getScore(): Int?
}

fun JobProjection.toDto(): JobDTO = JobDTO(
    id = getId(),
    date = getDate(),
    userName = getUserName(),
    userLastName = getUserLastName(),
    profession = getProfession(),
    status = getStatus(),
    price = getPrice(),
    score = getScore() ?: 0,
)
data class JobDTO(
    val id: Long,
    val date: LocalDate,
    val userName: String,
    val userLastName: String,
    val profession: String,
    val status: String,
    val price: Double,
    val score: Int,
)



