package quickfix.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.RatingRepository
import quickfix.dto.rating.EditRating
import quickfix.dto.rating.RatingDTO
import quickfix.models.Rating
import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

@Service
class RatingService(
    val ratingRepository: RatingRepository,
    val userService: UserService,
    val jobService: JobService
) {

    fun findRatingsReceivedByUser(userId: Long, pageable: Pageable): Page<Rating> {
        return ratingRepository.findByUserToId(userId, pageable)
    }

    fun findRatingsMadeByUser(userId: Long, pageable: Pageable): Page<Rating> {
        return ratingRepository.findByUserFromId(userId, pageable)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun rateUser(ratingDTO: RatingDTO) {
        val userFrom = userService.getUserById(ratingDTO.userFromId)
        val userTo = userService.getUserById(ratingDTO.userToId)
        val job = jobService.getJobById(ratingDTO.jobId)

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

    @Transactional(rollbackFor = [Exception::class])
    fun updateRating(id: Long, data: EditRating) {
        val rating = ratingRepository.findById(id).orElseThrow { BusinessException("Rating no existe") }
        rating.apply {
            data.score?.let { this.score = it }
            data.comment?.let { rating.comment = it }
        }.also { it.validate() }
    }
}