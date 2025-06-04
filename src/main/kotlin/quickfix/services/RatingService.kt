package quickfix.services

import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.RatingRepository
import quickfix.dto.rating.RateUserPageDTO
import quickfix.dto.rating.RatingDTO
import quickfix.dto.rating.RatingInfoDTO
import quickfix.models.Job
import quickfix.models.Rating
import quickfix.utils.PAGE_SIZE
import quickfix.utils.events.OnRatingEditedEvent
import quickfix.utils.events.OnRatingReceivedEvent
import quickfix.utils.exceptions.RatingException
import java.time.LocalDate

@Service
class RatingService(
    val ratingRepository: RatingRepository,
    val jobService: JobService,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun findCustomerRatings(customerId: Long, pageNumber: Int, ratingValue: Int): Page<Rating> {
        if(ratingValue < 0 || ratingValue > 5 || pageNumber < 0)
            throw RatingException("Ha habido un error en los parámetros solicitados.")

        val page = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "year_and_month")
        val ratingsPage =
            if(ratingValue > 0) ratingRepository.findByCustomerToIdAndScore(customerId, ratingValue, page)
            else ratingRepository.findAllByCustomerToId(customerId, page)

        return ratingsPage
    }

    fun findProfessionalRatings(professionalId: Long, pageNumber: Int, ratingValue: Int): Page<Rating> {
        if(ratingValue < 0 || ratingValue > 5 || pageNumber < 0)
            throw RatingException("Ha habido un error en los parámetros solicitados.")

        val page = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "year_and_month")
        val ratingsPage =
            if(ratingValue > 0) ratingRepository.findByProfessionalToIdAndScore(professionalId, ratingValue, page)
            else ratingRepository.findAllByProfessionalToId(professionalId, page)

        return ratingsPage
    }

    fun getJobRatingAsCustomer(customerId: Long, jobId: Long) : RateUserPageDTO {
        jobService.assertUserExistsInJob(customerId, jobId)
        val job = jobService.getJobById(jobId)
        val rating : Rating? = ratingRepository.findByCustomerFromIdAndJobId(customerId, jobId)
        return RateUserPageDTO.from(job.professional, job.profession.id, rating)
    }

    fun getJobRatingAsProfessional(professionalId: Long, jobId: Long) : RateUserPageDTO {
        jobService.assertUserExistsInJob(professionalId, jobId)
        val job = jobService.getJobById(jobId)
        val rating : Rating? = ratingRepository.findByProfessionalFromIdAndJobId(professionalId, jobId)
        return RateUserPageDTO.from(job.customer, job.profession.id, rating)
    }

    fun findRatingsReceivedByCustomer(customerId: Long, pageNumber: Int): Page<Rating> {
        val page = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "year_and_month")
        return ratingRepository.findAllByCustomerToId(customerId, page)
    }

    fun findRatingsReceivedByProfessional(professionalId: Long, pageNumber: Int): Page<Rating> {
        val page = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "year_and_month")
        return ratingRepository.findAllByProfessionalToId(professionalId, page)
    }

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
        eventPublisher.publishEvent(OnRatingReceivedEvent(rating))
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

        val rating = ratingRepository.findByJobIdAndUserFromId(data.jobId, userId).orElseThrow {RatingException("Ha habido un error al modificar esta calificación.")}
        val raterIsCustomer = userId == rating.job.customer.id

        if(raterIsCustomer)
            changeProfessionalScoreRating(rating, data.score)
        else
            changeCustomerScoreRating(rating, data.score)

        rating.apply {
            score = data.score
            comment = data.comment
        }.also { it.validate() }
        eventPublisher.publishEvent(OnRatingEditedEvent(rating))
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