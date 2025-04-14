package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.JobRepository
import quickfix.dao.RatingRepository
import quickfix.dao.UserRepository
import quickfix.dto.rating.RatingDTO
import quickfix.models.Rating

@Service
class RatingService(
    val userRepository: UserRepository,
    val jobRepository: JobRepository,
    val ratingRepository: RatingRepository
) {
    fun rateUser(ratingDTO: RatingDTO) {
        val userFrom = userRepository.findById(ratingDTO.userFromId)
        val userTo = userRepository.findById(ratingDTO.userToId)
        val job = jobRepository.findById(ratingDTO.jobId)
        val rating = Rating().apply {
            this.userFrom
            this.userTo
            this.job
            this.score
            this.yearAndMonth
            this.comment
        }.validate()
    }

    fun findRatingsReceivedByUser(userId: Long): List<Rating> {
        return ratingRepository.findByUserToId(userId)
    }

    fun findRatingsMadeByUser(userId: Long): List<Rating> {
        return ratingRepository.findByUserFromId(userId)
    }

}