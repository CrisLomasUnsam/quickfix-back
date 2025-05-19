package quickfix.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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
import quickfix.dto.chat.MessageDTO
import quickfix.dto.chat.MessageResponseDTO
import quickfix.dto.chat.toMessageResponseDTO
import quickfix.dto.job.JobDTO
import quickfix.dto.job.JobProjection
import quickfix.dto.job.MyJobDTO
import quickfix.dto.job.toDto
import quickfix.dto.professional.ProfessionalDTO
import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.PAGE_SIZE
import quickfix.utils.enums.JobStatus
import quickfix.utils.exceptions.JobException
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
        jobRepository.findById(id).orElseThrow { throw JobException("Ha habido un error al recuperar la información del trabajo.") }


    @Transactional(readOnly = true)
    fun findJobsByCustomerId(id: Long, pageNumber: Int): Page<MyJobDTO> {
        val pageable = sortPage(pageNumber)
        return jobRepository.findAllByCustomerId(id, pageable).map { it.toDto() }
    }


    @Transactional(readOnly = true)
    fun findJobsByProfessionalId(id: Long, pageNumber: Int): Page<MyJobDTO> {
        val pageable = sortPage(pageNumber)
        return jobRepository.findAllByProfessionalId(id, pageable).map { it.toDto() }

    }
    private fun sortPage(pageNumber: Int) : PageRequest {
        val sort: Sort = Sort.by("date").ascending()
        return PageRequest.of(pageNumber, PAGE_SIZE, sort)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun setJobAsDone(professionalId: Long, jobId: Long) {
        if(!jobRepository.existsByIdAndProfessionalId(jobId, professionalId))
            throw JobException("Ha habido un error al modificar el estado de este trabajo.")
        updateJobStatus(jobId,  true,  JobStatus.DONE)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun setJobAsCancelled(userId: Long, jobId: Long) {
        val userIsProfessional = jobRepository.existsByIdAndProfessionalId(jobId, userId)
        val userIsCustomer = jobRepository.existsByIdAndProfessionalId(jobId, userId)

        if(!userIsProfessional && !userIsCustomer)
            throw JobException("Ha habido un error al modificar el estado de este trabajo.")
        updateJobStatus(jobId,  false, JobStatus.CANCELED)
    }

    private fun updateJobStatus(id: Long, done: Boolean, status: JobStatus) {
        val job = this.getJobById(id)
        job.done = done
        job.status = status
    }

    @Transactional(readOnly = true)
    fun getJobsByParameter(id: Long, parameter: String?): List<JobProjection> =
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
            val professional = userService.getById(createdJobOffer.professionalId)
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
        val professional = userService.getById(jobOffer.professionalId).professionalInfo
        professional.validateCanOfferJob()
        redisService.offerJob(jobOffer)
    }

    fun cancelJobOffer(cancelOfferJob: CancelJobOfferDTO) =
        redisService.removeJobOffer(cancelOfferJob.professionId, cancelOfferJob.customerId, cancelOfferJob.professionalId)

    @Transactional(rollbackFor = [Exception::class])
    fun acceptJobOffer(acceptedJob: AcceptedJobOfferDTO) {

        val customer: User = userService.getById(acceptedJob.customerId)
        val professional : User = userService.getById(acceptedJob.professionalId)
        val profession: Profession = professionService.getProfessionById(acceptedJob.professionId)

        val jobOffers : Set<CreateJobOfferDTO> = redisService.getJobOffers(acceptedJob.customerId)
        val jobOffer = jobOffers.firstOrNull { it.professionalId == acceptedJob.professionalId }
            ?: throw JobException("No existe oferta de este profesional para el usuario.")

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
        redisService.deleteChatMessages(job.id)
    }

    fun getCustomerChatMessages(customerId : Long, jobId : Long) : List<MessageResponseDTO> {
        if(!jobRepository.existsByIdAndCustomerId(jobId, customerId)) throw JobException("Ha habido un error al obtener los mensajes.")
        return redisService.getChatMessages(jobId).map { it.toMessageResponseDTO(true)}
    }

    fun getProfessionalChatMessages(professionalId : Long, jobId : Long) : List<MessageResponseDTO> {
        if(!jobRepository.existsByIdAndProfessionalId(jobId, professionalId)) throw JobException("Ha habido un error al obtener los mensajes.")
        return redisService.getChatMessages(jobId).map { it.toMessageResponseDTO(false)}
    }

    fun postCustomerChatMessage(customerId : Long, message: MessageDTO) {
        if(!jobRepository.existsByIdAndCustomerId(message.jobId, customerId)) throw JobException("Ha habido un error al enviar el mensaje.")
        redisService.sendChatMessage(true, message)
    }

    fun postProfessionalChatMessage(professionalId : Long, message: MessageDTO) {
        if(!jobRepository.existsByIdAndProfessionalId(message.jobId, professionalId)) throw JobException("Ha habido un error al enviar el mensaje.")
        redisService.sendChatMessage(false, message)
    }

    fun getCustomerChatInfo(customerId: Long, jobId: Long): User {
        if (!jobRepository.existsByIdAndCustomerId(jobId, customerId)) throw JobException("Ha habido un error al obtener los datos solicitados.")
        val professionalId = jobRepository.getProfessionalIdByJobId(jobId)
        return userService.getById(professionalId)
    }

    fun getProfessionalChatInfo(professionalId: Long, jobId: Long): User {
        if (!jobRepository.existsByIdAndProfessionalId(jobId, professionalId)) throw JobException("Ha habido un error al obtener los datos solicitados.")
        val customerId = jobRepository.getCustomerIdByJobId(jobId)
        return userService.getById(customerId)
    }

}