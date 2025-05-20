package quickfix.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.JobRepository
import quickfix.dto.job.jobOffer.AcceptJobOfferDTO
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobOffer.CustomerJobOfferDTO
import quickfix.dto.job.jobRequest.ProfessionalJobRequestDTO
import quickfix.dto.chat.MessageDTO
import quickfix.dto.chat.MessageResponseDTO
import quickfix.dto.chat.toMessageResponseDTO
import quickfix.dto.job.jobOffer.ProfessionalJobOfferDTO
import quickfix.dto.job.jobRequest.CustomerJobRequestDTO
import quickfix.dto.job.jobRequest.validate
import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.PAGE_SIZE
import quickfix.utils.enums.JobStatus
import quickfix.utils.exceptions.JobException
import quickfix.utils.jobs.dateTimesCollides
import quickfix.utils.jobs.getJobEndtime
import quickfix.utils.jobs.getJobOfferEndtime
import java.time.LocalDateTime

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

    fun findJobsByCustomerId(id: Long, pageNumber: Int): Page<Job>  =
         jobRepository.findAllByCustomerId(id, sortPage(pageNumber))

    fun findJobsByProfessionalId(id: Long, pageNumber: Int): Page<Job> =
         jobRepository.findAllByProfessionalId(id, sortPage(pageNumber))

    private fun sortPage(pageNumber: Int) : PageRequest {
        val sort: Sort = Sort.by("date").ascending()
        return PageRequest.of(pageNumber, PAGE_SIZE, sort)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun setJobAsDone(professionalId: Long, jobId: Long) {
        if(!jobRepository.existsByIdAndProfessionalId(jobId, professionalId))
            throw JobException("Ha habido un error al modificar el estado de este trabajo.")
        updateJobStatus(jobId, JobStatus.DONE)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun setJobAsCancelled(userId: Long, jobId: Long) {
        val userIsProfessional = jobRepository.existsByIdAndProfessionalId(jobId, userId)
        val userIsCustomer = jobRepository.existsByIdAndProfessionalId(jobId, userId)

        if(!userIsProfessional && !userIsCustomer)
            throw JobException("Ha habido un error al modificar el estado de este trabajo.")
        updateJobStatus(jobId, JobStatus.CANCELED)
    }

    private fun updateJobStatus(id: Long, status: JobStatus) {
        val job = this.getJobById(id)
        job.status = status
    }

    fun getJobsByParameter(id: Long, parameter: String?): List<Job> =
        jobRepository.findJobByFilter(id, parameter)

    /*************************
     JOB REQUEST METHODS
     **************************/

    @Transactional(readOnly = true)
    fun getMyJobRequests(customerId : Long) : List<CustomerJobRequestDTO> {
        val myJobRequests = redisService.getMyJobRequests(customerId)
        return myJobRequests.map{ CustomerJobRequestDTO.fromJobRequest(it, redisService.countOffersForRequest(it)) }
    }


    @Transactional(readOnly = true)
    fun getJobRequests(professionalId : Long) : List<ProfessionalJobRequestDTO> {
        val professionIds : Set<Long> = professionalService.getActiveProfessionIds(professionalId)
        return redisService.getJobRequests(professionIds)
    }

    fun requestJob(jobRequest : ProfessionalJobRequestDTO) {
        userService.assertUserExists(jobRequest.customerId)
        professionService.assertProfessionExists(jobRequest.professionId)
        redisService.requestJob(jobRequest.apply{ validate() })
    }

    fun cancelJobRequest (customerId: Long, professionId: Long) {
        userService.assertUserExists(customerId)
        professionService.getProfessionById(professionId)
        redisService.removeJobRequest(customerId, professionId)
    }

    /*************************
     JOB OFFER METHODS
     **************************/

    fun getJobOffers(customerId : Long, professionId: Long): List<CustomerJobOfferDTO> {
        val jobOffers = redisService.getJobOffers(customerId, professionId)
        return jobOffers.map { CustomerJobOfferDTO.fromDto(it) }
    }

    fun getMyJobOffers(professionalId : Long): List<ProfessionalJobOfferDTO> {
        val myJobOffers = redisService.getMyJobOffers(professionalId)
        return myJobOffers.map { ProfessionalJobOfferDTO.fromDto(it) }
    }

    fun offerJob(professionalId: Long, jobOffer : JobOfferDTO) {
        if(professionalId != jobOffer.professional.id)
            throw JobException("Ha habido un error al intentar realizar esta oferta de trabajo.")
        assertProfessionalCanOfferJob(jobOffer)
        redisService.offerJob(jobOffer)
    }

    private fun assertProfessionalCanOfferJob(offerJob: JobOfferDTO) {
        val professional = userService.getProfessionalInfo(offerJob.professional.id)
        professional.validateCanOfferJob()
        assertPendingJobsDoNotCollide(offerJob)
    }

    private fun assertPendingJobsDoNotCollide(jobOffer: JobOfferDTO) {
        val openJobs = jobRepository.findOpenJobsByProfessionalId(jobOffer.professional.id)
        val jobOfferEndDatetime = getJobOfferEndtime(jobOffer)
        openJobs.forEach { openJob ->
            val openJobStartDatetime = openJob.initDateTime
            val openJobEndDatetime = getJobEndtime(openJob)
            if(dateTimesCollides(jobOffer.neededDatetime, jobOfferEndDatetime, openJobStartDatetime, openJobEndDatetime))
                throw JobException("Usted ya tiene un trabajo activo en este rango de tiempo.")
        }
    }

    fun cancelJobOffer(professionalId: Long, requestId: String) {
        val (professionId, customerId) = requestId.split("_")
        redisService.removeJobOffer(professionId.toLong(), customerId.toLong(), professionalId)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun acceptJobOffer(customerId: Long, acceptedJob: AcceptJobOfferDTO) {

        val jobOffers : List<JobOfferDTO> = redisService.getJobOffers(customerId, acceptedJob.professionId)
        val jobOffer = jobOffers.firstOrNull { it.professional.id == acceptedJob.professionalId }
            ?: throw JobException("La oferta ha expirado.")

        val customer: User = userService.getById(customerId)
        val professional : User = userService.getById(acceptedJob.professionalId)
        val profession: Profession = professionService.getProfessionById(acceptedJob.professionId)

        val job = Job().apply {
            this.professional = professional
            this.customer = customer
            this.profession = profession
            this.price = jobOffer.price
            this.initDateTime = if(jobOffer.instantRequest) LocalDateTime.now() else jobOffer.neededDatetime
            this.duration = jobOffer.jobDuration
            this.durationUnit = jobOffer.jobDurationTimeUnit
        }

        jobRepository.save(job)
        redisService.removeJobRequest(profession.id, customerId)
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
        val professionalId = jobRepository.findProfessionalIdByJobId(jobId)
        return userService.getById(professionalId)
    }

    fun getProfessionalChatInfo(professionalId: Long, jobId: Long): User {
        if (!jobRepository.existsByIdAndProfessionalId(jobId, professionalId)) throw JobException("Ha habido un error al obtener los datos solicitados.")
        val customerId = jobRepository.findCustomerIdByJobId(jobId)
        return userService.getById(customerId)
    }

}