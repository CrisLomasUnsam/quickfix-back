package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.JobRepository
import quickfix.dao.RatingRepository
import quickfix.dao.UserRepository
import quickfix.dto.rating.EditRating
import quickfix.dto.rating.RatingDTO
import quickfix.models.Rating
import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

@Service
class RatingService(
    val userRepository: UserRepository,
    val jobRepository: JobRepository,
    val ratingRepository: RatingRepository
) {
    @Transactional
    fun rateUser(ratingDTO: RatingDTO) {
        val userFrom = userRepository.findById(ratingDTO.userFromId).orElseThrow {
            BusinessException("Usuario no existe")
        }
        val userTo = userRepository.findById(ratingDTO.userToId).orElseThrow {
            BusinessException("Usuario no existe")
        }
        val job = jobRepository.findById(ratingDTO.jobId).orElseThrow {
            BusinessException("Job no existe")
        }

        val rating = Rating().apply {
            this.userFrom = userFrom
            this.userTo = userTo
            this.job = job
            this.score = ratingDTO.score
            this.yearAndMonth = LocalDate.now()
            this.comment = ratingDTO.comment
        }.also { it.validate() }
        ratingRepository.save(rating)
    }

    fun findRatingsReceivedByUser(userId: Long): List<Rating> {
        return ratingRepository.findByUserToId(userId).orElseThrow{ BusinessException("Usuario no existe") }
    }

    fun findRatingsMadeByUser(userId: Long): List<Rating> {
        return ratingRepository.findByUserFromId(userId).orElseThrow { BusinessException("Usuario no existe") }
    }

    @Transactional
    fun updateRating(id: Long, data: EditRating) {
        val rating = ratingRepository.findById(id).orElseThrow { BusinessException("Rating no existe") }
        rating.apply {
            data.score?.let { this.score = it }
            data.comment?.let { rating.comment = it }
        }.also { it.validate() }
    }
}