package quickfix.dto.job

import quickfix.utils.functions.getAvatarUrl
import java.time.LocalDate

interface MyJobProjection {
    fun getId(): Long
    fun getDate(): LocalDate
    fun getUserId(): Long?
    fun getUserName(): String
    fun getUserLastName(): String
    fun getProfession(): String
    fun getStatus(): String
    fun getPrice(): Double
    fun getScore(): Int?


}

fun MyJobProjection.toDto(): MyJobDTO = MyJobDTO(
    id = getId(),
    date = getDate(),
    avatar = getAvatarUrl(getUserId() ?: 0L),
    userName = getUserName(),
    userLastName = getUserLastName(),
    professionName = getProfession(),
    status = getStatus(),
    price = getPrice(),
    score = getScore() ?: 0,
)
data class MyJobDTO(
    val id: Long,
    val date: LocalDate,
    val avatar: String,
    val userName: String,
    val userLastName: String,
    val professionName: String,
    val status: String,
    val price: Double,
    val score: Int,
)
