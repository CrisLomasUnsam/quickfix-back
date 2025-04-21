package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.JobRepository
import quickfix.dto.job.jobOffer.AcceptedJobOfferDTO
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.message.ChatMessageDTO
import quickfix.dto.message.RedisMessageDTO
import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException

@Service
class JobService(
    val jobRepository: JobRepository,
    val redisService: RedisService,
    val userService: UserService,
    val professionService: ProfessionService
){
  
    fun createJob(job: Job) {
        jobRepository.save(job)
    }


    fun deleteJob(job: Job) =
        jobRepository.delete(job)

    fun getJobById(id: Long): Job = jobRepository.findById(id).orElseThrow { throw BusinessException() }

    @Transactional(rollbackFor = [Exception::class])
    fun acceptJobOffer(acceptedJob: AcceptedJobOfferDTO) {

        val customer: User = userService.getUserById(acceptedJob.customerId)
        val professional : User = userService.getUserById(acceptedJob.professionalId)
        val profession: Profession = professionService.getProfessionById(acceptedJob.professionId)
        val jobOffers : Set<JobOfferDTO> = redisService.getJobOffers(acceptedJob.customerId)

        val offer = jobOffers.firstOrNull { it.professional.id == acceptedJob.professionalId } ?: throw BusinessException("…")

        println(offer)

        //Limpia el request y offer me parecio conveniente borrarla inmpediatamente a pesar de q tenga el ttl
        redisService.removeJobRequest(profession.id, acceptedJob.customerId)
        redisService.removeJobOffer(profession.id, acceptedJob.customerId, acceptedJob.professionalId)
    }

//    fun getJobsByUser(id: Long) = jobRepository.getAllByUserId(id)

//    fun setJobAsDone(id: Long) = jobRepository.setToDone(id)
//
//    fun setJobAsCancelled(id: Long) = jobRepository.setToCancelled(id)

//    fun getJobsByParameter(id: Long, parameter: String): List<Job> {
//        val searchParameters = JobSearchParameters(parameter)
//        return jobRepository.searchByParameters(id, searchParameters)
//    }

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