package quickfix.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.RatingRepository
import quickfix.dto.rating.*
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
        return ratingRepository.findAllByUserToId(userId, pageable)
    }

    fun findRatingsMadeByUser(userId: Long): List<Rating> {
        return ratingRepository.findAllByUserFromId(userId)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun rateUser(currentUserId: Long, ratingDTO: RatingDTO) {

        val userFrom = userService.getById(currentUserId)
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
        //TODO: Validar que el usuario pueda editar este rating
        rating.apply {
            data.score?.let { this.score = it }
            data.comment?.let { rating.comment = it }
        }.also { it.validate() }
    }

    fun getSeeProfile(currentUserId: Long): UserProfileDTO {
        val user = userService.getById(currentUserId)
        val totalJobs = jobService.countFinishedJobsForUser(user.id)
        val professions  = user.professionalInfo.activeProfessionNames()
        val stats = ratingRepository.findRatingStatsForUser(user.id)
        return UserProfileDTO.from(user, totalJobs, professions, stats)
    }
}