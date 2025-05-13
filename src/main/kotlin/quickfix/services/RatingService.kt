package quickfix.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.RatingRepository
import quickfix.dto.rating.EditRatingDTO
import quickfix.dto.rating.RateUserPageDTO
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


    fun rateUser(currentUserId: Long, ratingDTO: RatingDTO) {

        val userFrom = userService.getUserById(currentUserId)
        val job = jobService.getJobById(ratingDTO.jobId)

        if (currentUserId != job.customer.id && currentUserId != job.professional.id) {
            throw BusinessException("Usted no participó en ese job y no puede calificar.")
        }

        val alreadyRated = ratingRepository
            .existsByJobIdAndUserFromId(job.id, userFrom.id)
        if (alreadyRated) {
            throw BusinessException("Ya ha calificado este job anteriormente.")
        }

        val userTo = if (currentUserId == job.customer.id) job.professional else job.customer

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

        if (rating.userFrom.id != userId) {
            throw BusinessException("No tienes permiso para editar esta calificación")
        }
        rating.apply {
            data.score?.let { this.score = it }
            data.comment?.let { rating.comment = it }
        }
        rating.validate()
    }

    fun jobRating(currentUserId: Long, jobId: Long): RateUserPageDTO{
        val job = jobService.getJobById(jobId)

        if (currentUserId != job.customer.id && currentUserId != job.professional.id) {
            throw BusinessException("Usted no participó en este job")
        }

        val userTo = if (currentUserId == job.customer.id)  job.professional else job.customer

        val existingRating = ratingRepository
            .findByJobIdAndUserFromId(jobId, currentUserId)


        val avg = ratingRepository.findAverageRatingByUserToId(userTo.id)

        return RateUserPageDTO.from(userTo, avg, existingRating, job)
    }
}