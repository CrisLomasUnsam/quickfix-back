package quickfix.dto.job

import java.time.LocalDate

// Proyección para jobs que incluyen calificación
interface JobWithRatingProjection {
    fun getId(): Long
    fun getDate(): LocalDate
    fun getUserName(): String
    fun getUserLastName(): String
    fun getProfession(): String
    fun getStatus(): String
    fun getPrice(): Double
    fun getScore(): Int
}

// Mapper a DTO detallado con rating
fun JobWithRatingProjection.toDto(): JobWithRatingDTO = JobWithRatingDTO(
    id = getId(),
    date = getDate(),
    userName = getUserName(),
    userLastName = getUserLastName(),
    profession = getProfession(),
    status = getStatus(),
    price = getPrice(),
    score = getScore()
)

// DTO detallado con rating
data class JobWithRatingDTO(
    val id: Long,
    val date: LocalDate,
    val userName: String,
    val userLastName: String,
    val profession: String,
    val status: String,
    val price: Double,
    val score: Int,
)


// Proyección básica sin calificación
interface JobBasicInfoProjection {
    fun getId(): Long
    fun getDate(): LocalDate
    fun getUserName(): String
    fun getProfession(): String
    fun getStatus(): String
    fun getPrice(): Double
}

// Mapper a DTO básico
fun JobBasicInfoProjection.toDto() = JobBasicInfoDTO(
    id                   = getId(),
    date                 = getDate(),
    userName             = getUserName(),
    profession           = getProfession(),
    status               = getStatus(),
    price                = getPrice(),
)


// DTO básico sin rating
data class JobBasicInfoDTO(
    val id: Long,
    val date: LocalDate,
    val userName: String,
    val profession: String,
    val status: String,
    val price: Double,
)