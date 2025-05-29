package quickfix.dto.job

import com.fasterxml.jackson.annotation.JsonFormat
import quickfix.utils.enums.JobStatus
import quickfix.utils.functions.getAvatarUrl
import java.time.LocalDateTime

interface IJobCard {
    fun getId(): Long
    fun getInitDateTime(): LocalDateTime
    fun getUserName(): String
    fun getUserLastName(): String
    fun getUserVerified(): Boolean
    fun getProfessionId(): Long
    fun getStatus(): JobStatus
    fun getPrice(): Double
    fun getScore(): Int?
    fun getUserId(): Long
}

data class JobCardDTO(
    val id: Long,
    @JsonFormat(pattern = "dd/MM/yyyy")
    val initDateTime: LocalDateTime,
    val userName: String,
    val userLastName: String,
    val avatar: String,
    val verified: Boolean,
    val professionId: Long,
    val status: JobStatus,
    val price: Double,
    val score: Int?
) {
    companion object {
        fun fromProjection(job : IJobCard) : JobCardDTO =
            JobCardDTO(
                id = job.getId(),
                initDateTime = job.getInitDateTime(),
                userName = job.getUserName(),
                userLastName = job.getUserLastName(),
                verified = job.getUserVerified(),
                avatar = getAvatarUrl(job.getUserId()),
                professionId = job.getProfessionId(),
                status = job.getStatus(),
                price = job.getPrice(),
                score = job.getScore()
            )
    }
}