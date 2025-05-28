package quickfix.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.JobRepository
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.job.jobOffer.AcceptJobOfferDTO
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobOffer.CustomerJobOfferDTO
import quickfix.dto.chat.MessageDTO
import quickfix.dto.chat.MessageResponseDTO
import quickfix.dto.chat.toMessageResponseDTO
import quickfix.dto.job.JobDetailsDTO
import quickfix.dto.job.JobWithRatingDTO
import quickfix.dto.job.jobOffer.*
import quickfix.dto.job.jobRequest.validate
import quickfix.dto.job.jobRequest.CustomerJobRequestDTO
import quickfix.dto.job.jobRequest.ProfessionalJobRequestDTO
import quickfix.dto.job.jobOffer.ProfessionalJobOfferDTO
import quickfix.dto.job.jobRequest.*
import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.PAGE_SIZE
import quickfix.utils.enums.JobStatus
import quickfix.utils.exceptions.JobException
import quickfix.utils.jobs.dateTimesCollides
import quickfix.utils.jobs.getJobEndtime
import quickfix.utils.jobs.getJobOfferEndtime
import quickfix.utils.jobs.getJobRequestKey
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

    @Transactional(readOnly = true)
    fun findMyJobsByCustomerId(customerId: Long, pageNumber: Int?): Page<JobWithRatingDTO> =
        jobRepository.findAllJobsByCustomerId(customerId, getMyJobsPageRequest(pageNumber)).map { JobWithRatingDTO.fromProjection(it) }

    @Transactional(readOnly = true)
    fun findMyJobsByProfessionalId(professionalId: Long, pageNumber: Int?): Page<JobWithRatingDTO> =
        jobRepository.findAllJobsByProfessionalId(professionalId, getMyJobsPageRequest(pageNumber)).map { JobWithRatingDTO.fromProjection(it) }

    @Transactional(rollbackFor = [Exception::class])
    fun setJobAsDone(professionalId: Long, jobId: Long) {
        assertUserExistsInJob(professionalId, jobId)
        updateJobStatus(jobId, JobStatus.DONE)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun setJobAsCancelled(userId: Long, jobId: Long) {
        assertUserExistsInJob(userId, jobId)
        updateJobStatus(jobId, JobStatus.CANCELED)
    }

    private fun updateJobStatus(id: Long, status: JobStatus) {
        val job = this.getJobById(id)
        job.status = status
    }

    private fun getMyJobsPageRequest(pageNumber: Int?) : PageRequest? {
        if(pageNumber == null) return null
        return PageRequest.of(pageNumber, PAGE_SIZE)
    }

    fun getJobDetailsById(currentUserId: Long, jobId: Long): JobDetailsDTO {

        assertUserExistsInJob(currentUserId, jobId)

        val job = getJobById(jobId)
        val seeCustomerInfo = currentUserId == job.professional.id
        val totalRatings =
            if (seeCustomerInfo) { userService.getSeeCustomerProfileInfo(currentUserId).getTotalRatings() }
            else { userService.getSeeProfessionalProfileInfo(currentUserId).getTotalRatings() }

        return JobDetailsDTO.toDTO(currentUserId, job, seeCustomerInfo, totalRatings)
    }

    fun assertUserExistsInJob(userId: Long, jobId: Long){
        val userIsProfessional = jobRepository.existsByIdAndProfessionalId(jobId, userId)
        val userIsCustomer = jobRepository.existsByIdAndCustomerId(jobId, userId)

        if(!userIsProfessional && !userIsCustomer)
            throw JobException("El usuario actual no corresponde a este trabajo.")
    }

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
        val myProfessionIds : Set<Long> = professionalService.getActiveProfessionIds(professionalId)
        val myJobOfferKeys = getMyJobOffers(professionalId).map{ it.requestId }.toSet()
        return redisService.getJobRequests(myProfessionIds, myJobOfferKeys).map { ProfessionalJobRequestDTO.fromJobRequest(it) }
    }

    @Transactional(readOnly = true)
    fun getNotOfferedJobRequest(professionalId : Long, jobRequestId: String) : JobRequestDTO {
        val jobRequest = redisService.getJobRequest(jobRequestId) ?: throw JobException("La solicitud expiró.")
        validateCanViewNotOfferedRequest(professionalId, jobRequest)
        return jobRequest
    }

    private fun validateCanViewNotOfferedRequest(professionalId: Long, jobRequest: JobRequestDTO) {
        val myProfessionIds : Set<Long> = professionalService.getActiveProfessionIds(professionalId)
        if(!myProfessionIds.contains(jobRequest.professionId))
            throw JobException("Usted no puede ver esta solicitud.")

        val myOfferedRequestKeys = getMyJobOffers(professionalId).map{ it.requestId }
        val requestIsAlreadyOffered = myOfferedRequestKeys.contains(getJobRequestKey(jobRequest.professionId, jobRequest.customer.id))
        if(requestIsAlreadyOffered)
            throw JobException("Usted ya tiene una oferta activa para esta solicitud.")
    }

    fun requestJob(currentCustomerId: Long, jobRequest : CreateJobRequestDTO) {

        jobRequest.validate()
        userService.assertUserExists(currentCustomerId)
        professionService.assertProfessionExists(jobRequest.professionId)

        val customerDto = SeeBasicUserInfoDTO.toDTO(userService.getById(currentCustomerId), seeCustomerInfo = true)
        val jobRequestDto = jobRequest.toJobRequestDTO(customerDto).apply { validate() }

        redisService.requestJob(jobRequestDto)
    }

    fun cancelJobRequest (professionId: Long, customerId: Long) {
        userService.assertUserExists(customerId)
        professionService.assertProfessionExists(professionId)
        redisService.removeJobRequest(professionId, customerId)
    }

    /*************************
     JOB OFFER METHODS
     **************************/

    fun getJobOffers(customerId : Long, professionId: Long): List<CustomerJobOfferDTO> {
        val jobOffers = redisService.getJobOffers(professionId, customerId)
        return jobOffers.map { CustomerJobOfferDTO.fromDto(it) }
    }

    fun getMyJobOffers(professionalId : Long): List<ProfessionalJobOfferDTO> {
        val myJobOffers = redisService.getMyJobOffers(professionalId)
        return myJobOffers.map { ProfessionalJobOfferDTO.fromDto(it) }
    }

    fun offerJob(professionalId: Long, newJobOffer: CreateJobOfferDTO) {
        val request = redisService.getJobRequest(newJobOffer.requestId) ?: throw JobException("La oferta ha expirado.")
        val professional = userService.getById(professionalId)
        val offer = CreateJobOfferDTO.toJobOffer(newJobOffer, request, professional)
        assertProfessionalCanOfferJob(professional, offer)
        redisService.offerJob(offer)
    }

    private fun assertProfessionalCanOfferJob(professional : User, jobOffer: JobOfferDTO) {
        professional.professionalInfo.validateCanOfferJob()
        assertPendingJobsDoNotCollide(jobOffer)
    }

    private fun assertPendingJobsDoNotCollide(jobOffer: JobOfferDTO) {
        val openJobs = jobRepository.findOpenJobsByProfessionalId(jobOffer.professional.id)
        val offerEndDatetime = getJobOfferEndtime(jobOffer)
        openJobs.forEach { openJob ->
            val openJobStartDatetime = openJob.initDateTime
            val openJobEndDatetime = getJobEndtime(openJob)
            if(dateTimesCollides(jobOffer.request.neededDatetime, offerEndDatetime, openJobStartDatetime, openJobEndDatetime))
                throw JobException("Usted ya tiene un trabajo activo en este rango de tiempo.")
        }
    }

    fun cancelJobOffer(professionalId: Long, requestId: String) {
        val jobOffer = getMyJobOffers(professionalId).find { it.requestId == requestId } ?: throw JobException("Esta oferta ya no existe.")
        val request = jobOffer.request
        redisService.removeJobOffer(request.professionId, request.customer.id, professionalId)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun acceptJobOffer(customerId: Long, acceptedJob: AcceptJobOfferDTO) {

        val jobOffers : List<JobOfferDTO> = redisService.getJobOffers(acceptedJob.professionId, customerId)
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
            this.initDateTime = if(jobOffer.request.instantRequest) LocalDateTime.now() else jobOffer.request.neededDatetime
            this.duration = jobOffer.duration
            this.durationUnit = jobOffer.durationUnit
            this.detail = jobOffer.request.detail
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