package quickfix.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.RatingRepository
import quickfix.dto.rating.EditRatingDTO
import quickfix.dto.rating.RatingDTO
import quickfix.models.Rating
import quickfix.utils.exceptions.BusinessException
import quickfix.utils.exceptions.RatingException
import java.time.LocalDate

@Service
class RatingService(
    val ratingRepository: RatingRepository,
    val userService: UserService,
    val jobService: JobService
) {

    fun findRatingsReceivedByUser(userId: Long, pageable: Pageable): Page<Rating> {
        return ratingRepository.findAllByUserToId(userId, pageable)
    }

    fun findRatingsMadeByUser(userId: Long): List<Rating> {
        return ratingRepository.findAllByUserFromId(userId)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun rateUser(currentUserId: Long, ratingDTO: RatingDTO) {
        if(currentUserId != ratingDTO.userFromId)
            throw RatingException("Usted no puede calificar a este usuario")

        val userFrom = userService.getUserById(ratingDTO.userFromId)
        val userTo = userService.getUserById(ratingDTO.userToId)
        val job = jobService.getJobById(ratingDTO.jobId)

        if(job.customer.id != currentUserId && job.professional.id != currentUserId)
            throw RatingException("Usted no puede calificar a este usuario")

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
    fun updateRating(userId: Long, data: EditRatingDTO) {
        val rating = ratingRepository.findById(data.ratingId).orElseThrow { BusinessException("Rating no existe") }
        //TODO: Validar que el usuario pueda editar este rating
        rating.apply {
            data.score?.let { this.score = it }
            data.comment?.let { rating.comment = it }
        }.also { it.validate() }
    }
}