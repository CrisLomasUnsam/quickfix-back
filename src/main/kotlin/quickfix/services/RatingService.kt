package quickfix.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.RatingRepository
import quickfix.dto.rating.RatingDTO
import quickfix.dto.rating.RatingInfoDTO
import quickfix.models.Job
import quickfix.models.Rating
import quickfix.utils.exceptions.RatingException
import java.time.LocalDate

@Service
class RatingService(
    val ratingRepository: RatingRepository,
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
    fun rateUser(raterUserId: Long, ratingDTO: RatingDTO) {

        if (ratingRepository.existsByJobIdAndUserFromId(ratingDTO.jobId, raterUserId))
            throw RatingException("No puede agregar más calificaciones para este servicio.")

        val job : Job = jobService.getJobById(ratingDTO.jobId)
        val raterIsCustomer = raterUserId == job.customer.id
        val userFrom = if (raterIsCustomer) job.customer else job.professional
        val userTo = if (raterIsCustomer) job.professional else job.customer

        if(raterIsCustomer)
            addScoreRatingToProfessional(userTo.id, ratingDTO.score)
        else
            addScoreRatingToCustomer(userTo.id, ratingDTO.score)

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
    fun addScoreRatingToCustomer(customerId: Long, score: Int) {
        val ratingInfo = RatingInfoDTO.toDTO(ratingRepository.findCustomerRatingInfo(customerId))
        val newAverageRating = (ratingInfo.total + score) / (ratingInfo.amount + 1.0)
        ratingRepository.setAverageRatingForCustomer(customerId, newAverageRating)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun addScoreRatingToProfessional(professionalId: Long, score: Int) {
        val ratingInfo = RatingInfoDTO.toDTO(ratingRepository.findProfessionalRatingInfo(professionalId))
        val newAverageRating = (ratingInfo.total + score) / (ratingInfo.amount + 1.0)
        ratingRepository.setAverageRatingForProfessional(professionalId, newAverageRating)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updateRating(userId: Long, data: RatingDTO) {

        if(!ratingRepository.existsByJobIdAndUserFromId(data.jobId, userId))
            throw RatingException("Ha habido un error al modificar esta calificación.")

        val rating : Rating = getByJobId(data.jobId)
        val raterIsCustomer = userId == rating.job.customer.id

        if(raterIsCustomer)
            changeProfessionalScoreRating(rating, data.score)
        else
            changeCustomerScoreRating(rating, data.score)

        rating.apply {
            score = data.score
            comment = data.comment
        }.also { it.validate() }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun changeCustomerScoreRating(rating: Rating, newScore: Int) {
        val customerId = rating.userTo.id
        val ratingInfo = RatingInfoDTO.toDTO(ratingRepository.findCustomerRatingInfo(customerId))
        val newAverageRating = (ratingInfo.total + newScore - rating.score) / (ratingInfo.amount)
        ratingRepository.setAverageRatingForCustomer(customerId, newAverageRating)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun changeProfessionalScoreRating(rating: Rating, newScore: Int) {
        val professionalId = rating.userTo.id
        val ratingInfo = RatingInfoDTO.toDTO(ratingRepository.findProfessionalRatingInfo(professionalId))
        val newAverageRating = (ratingInfo.total + newScore - rating.score) / (ratingInfo.amount)
        ratingRepository.setAverageRatingForProfessional(professionalId, newAverageRating)
    }

}