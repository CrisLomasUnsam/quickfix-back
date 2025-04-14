package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.JobRepository
import quickfix.dao.RatingRepository
import quickfix.dao.UserRepository
import quickfix.dto.rating.RatingDTO
import quickfix.models.Rating
import quickfix.utils.exceptions.BusinessException

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
            this.yearAndMonth = ratingDTO.yearAndMonth
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

}