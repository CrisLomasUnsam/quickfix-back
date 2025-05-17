package quickfix.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.RatingRepository
import quickfix.dto.rating.RatingDTO
import quickfix.models.Rating
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

    @Transactional(readOnly = true)
    fun findRatingsMadeByUser(userId: Long): List<Rating> {
        return ratingRepository.findAllByUserFromId(userId)
    }

    private fun getByJobId(jobId: Long) : Rating =
        ratingRepository.findByJobId(jobId).orElseThrow { RatingException("Ha habido un error al obtener la calificación.") }

    @Transactional(rollbackFor = [Exception::class])
    fun rateUser(currentUserId: Long, ratingDTO: RatingDTO) {

        val userFrom = userService.getById(currentUserId)
        val job = jobService.getJobById(ratingDTO.jobId)

        if (currentUserId != job.customer.id && currentUserId != job.professional.id)
            throw RatingException("Usted no puede calificar este servicio.")

        if (ratingRepository.existsByJobIdAndUserFromId(job.id, userFrom.id))
            throw RatingException("No puede agregar más calificaciones para este servicio.")

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
    fun updateRating(userId: Long, data: RatingDTO) {

        if(!ratingRepository.existsByJobIdAndUserFromId(data.jobId, userId))
            throw RatingException("Ha habido un error al modificar esta calificación.")

        val rating = getByJobId(data.jobId)
        rating.apply {
            score = data.score
            comment = data.comment
        }.also { it.validate() }
    }

}