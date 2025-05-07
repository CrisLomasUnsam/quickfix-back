package quickfix.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.JobRepository
import quickfix.dto.job.jobOffer.AcceptedJobOfferDTO
import quickfix.dto.job.jobOffer.CancelJobOfferDTO
import quickfix.dto.job.jobOffer.CreateJobOfferDTO
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobRequest.CancelJobRequestDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.message.ChatMessageDTO
import quickfix.dto.message.RedisMessageDTO
import quickfix.dto.professional.ProfessionalDTO
import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.MAX_DEBT_ALLOWED
import quickfix.utils.PAGE_SIZE
import quickfix.utils.enums.JobStatus
import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

@Service
class JobService(
    val jobRepository: JobRepository,
    val redisService: RedisService,
    val userService: UserService,
    val professionalService: ProfessionalService,
    val professionService: ProfessionService
){

    fun getJobById(id: Long): Job =
        jobRepository.findById(id).orElseThrow { throw BusinessException() }

    fun findJobsByCustomerId(id: Long, pageNumber: Int): Page<Job>  =
         jobRepository.findAllByCustomerId(id, sortPage(pageNumber))



    fun findJobsByProfessionalId(id: Long, pageNumber: Int): Page<Job> =
         jobRepository.findAllByProfessionalId(id, sortPage(pageNumber))


    fun sortPage(pageNumber: Int) : PageRequest {
        val sort: Sort = Sort.by("date").ascending()
       return PageRequest.of(pageNumber, PAGE_SIZE, sort)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun setJobAsDone(id: Long) =
        updateJobStatus(id,  true,  JobStatus.DONE)

    @Transactional(rollbackFor = [Exception::class])
    fun setJobAsCancelled(id: Long) =
        updateJobStatus(id,  false, JobStatus.CANCELED)

    private fun updateJobStatus(id: Long, done: Boolean, status: JobStatus) {
        val job = this.getJobById(id)
        job.done = done
        job.status = status
    }

    fun getJobsByParameter(id: Long, parameter: String?): List<Job> =
        jobRepository.findJobByFilter(id, parameter)

    /*************************
     JOB REQUEST METHODS
     **************************/

    fun getJobRequests(professionalId : Long) : Set<JobRequestDTO> {
        val professionIds : Set<Long> = professionalService.getProfessionIds(professionalId)
        return redisService.getJobRequests(professionIds)
    }

    fun requestJob(jobRequest : JobRequestDTO) {

        userService.assertUserExists(jobRequest.customerId)
        professionService.assertProfessionExists(jobRequest.professionId)
        redisService.requestJob(jobRequest, jobRequest.professionId)
    }

    fun cancelJobRequest (cancelJobRequest : CancelJobRequestDTO) {
        val (customerId, professionId) = cancelJobRequest
        userService.assertUserExists(customerId)
        professionService.getProfessionById(professionId)
        redisService.removeJobRequest(customerId, professionId)
    }

    /*************************
     JOB OFFER METHODS
     **************************/

    fun getJobOffers(customerId : Long): List<JobOfferDTO> {
        val createdJobOffers = redisService.getJobOffers(customerId)

        return createdJobOffers.map { createdJobOffer ->
            val professional = userService.getUserById(createdJobOffer.professionalId)
            val professionalRating = jobRepository.findRatingsByProfessionalId(createdJobOffer.professionalId).map { it.score }.average()

            JobOfferDTO(
                customerId = createdJobOffer.customerId,
                professionId = createdJobOffer.professionId,
                professional = ProfessionalDTO.fromUser(professional, professionalRating),
                price = createdJobOffer.price,
                distance = createdJobOffer.distance,
                estimatedArriveTime = createdJobOffer.estimatedArriveTime,
                availability = createdJobOffer.availability,
            )
        }
    }

    fun offerJob(jobOffer : CreateJobOfferDTO) {
        val professional = userService.getUserById(jobOffer.professionalId).professionalInfo
        professional.validateCanOfferJob()
        redisService.offerJob(jobOffer)
    }

    fun cancelJobOffer(cancelOfferJob: CancelJobOfferDTO) =
        redisService.removeJobOffer(cancelOfferJob.professionId, cancelOfferJob.customerId, cancelOfferJob.professionalId)

    @Transactional(rollbackFor = [Exception::class])
    fun acceptJobOffer(acceptedJob: AcceptedJobOfferDTO) {

        val customer: User = userService.getUserById(acceptedJob.customerId)
        val professional : User = userService.getUserById(acceptedJob.professionalId)
        val profession: Profession = professionService.getProfessionById(acceptedJob.professionId)

        val jobOffers : Set<CreateJobOfferDTO> = redisService.getJobOffers(acceptedJob.customerId)
        val jobOffer = jobOffers.firstOrNull { it.professionalId == acceptedJob.professionalId }
            ?: throw BusinessException("No existe oferta de este profesional para el usuario.")

        val job = Job().apply {
            this.professional = professional
            this.customer = customer
            this.date = LocalDate.now()
            this.profession = profession
            this.price = jobOffer.price
        }

        jobRepository.save(job)
        redisService.removeJobRequest(profession.id, acceptedJob.customerId)
    }


    /*************************
        CHAT METHODS
    **************************/

    private fun deleteChatMessages(jobId: Long){
        val job = getJobById(jobId)
        redisService.deleteChatMessages(job.customer.id, job.professional.id, job.id)
    }

    fun getChatMessages(customerId : Long, professionalId : Long, jobId : Long) : List<RedisMessageDTO> {
        validateChatMessageIds(customerId, professionalId, jobId)
        return redisService.getChatMessages(customerId, professionalId, jobId)
    }

    fun postChatMessage(message: ChatMessageDTO) {
        validateChatMessageIds(message.customerId, message.professionalId, message.jobId)
        redisService.sendChatMessage(message)
    }

    private fun validateChatMessageIds(customerId : Long, professionalId : Long, jobId : Long) {
        val job = getJobById(jobId)
        val notValidIds = job.customer.id != customerId || job.professional.id != professionalId
        if(notValidIds)
            throw BusinessException("Ha habido un error. Por favor, verifique los datos.")
    }

}