package quickfix.services

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import quickfix.dto.job.jobOffer.CreateJobOfferDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.message.MessageDTO
import quickfix.dto.message.RedisMessageDTO
import quickfix.dto.message.toRedisMessage
import quickfix.utils.exceptions.BusinessException

@Service
class RedisService(

    private val redisJobRequestStorage: RedisTemplate<String, JobRequestDTO>,
    private val redisJobOfferStorage: RedisTemplate<String, CreateJobOfferDTO>,
    private val redisChatStorage: RedisTemplate<String, RedisMessageDTO>

) {

    /******************************************************
    JOB_REQUESTS WILL HAVE THE FOLLOWING KEY PATTERN:
    JobRequest_ProfessionId_CustomerId_
     *******************************************************/


    private fun getJobRequestKey(professionId: Long, customerId: Long) : String =
        "JobRequest_${professionId}_${customerId}_"

    fun requestJob(jobRequest : JobRequestDTO, professionId : Long) {

        val customerId = jobRequest.customerId

        val tempKey = "JobRequest_*_${customerId}_"
        val userHasPreviousRequest = redisJobRequestStorage.keys(tempKey).isNotEmpty()

        if(userHasPreviousRequest)
            throw BusinessException("Este usuario ya tiene una solicitud activa.")

        val key = getJobRequestKey(professionId, customerId)
        redisJobRequestStorage.opsForValue().set(key,jobRequest)
        //TODO: Creo que es conveniente agregar TTL así no bloqueamos eternamente a un usuario
        //redisJobRequestStorage.expire(key, Duration.ofMinutes(5))
    }

    fun getJobRequests(professionIds : Set<Long>) : Set<JobRequestDTO> {
        val jobRequests = mutableSetOf<JobRequestDTO>()
        professionIds.forEach { professionId ->
            val tempKey = "JobRequest_${professionId}_*_"
            val requestsKeys = redisJobRequestStorage.keys(tempKey)
            val requestsValues = redisJobRequestStorage.opsForValue().multiGet(requestsKeys) ?: emptySet()
            jobRequests.addAll(requestsValues)
        }
        return jobRequests
    }

    fun removeJobRequest(professionId : Long, customerId: Long) {
        val key = getJobRequestKey(professionId, customerId)

        if (!redisJobRequestStorage.hasKey(key))
            throw BusinessException("No existe una solicitud activa para el usuario $customerId en la profesión $professionId")

        redisJobRequestStorage.delete(key)
        this.removeAllJobOffers(professionId, customerId)
    }

    private fun validateCustomerHasAJobRequest(customerId : Long){
        val tempKey = "JobRequest_*_${customerId}_"
        val requestsKeys = redisJobRequestStorage.keys(tempKey)
        if(requestsKeys.isEmpty())
            throw BusinessException("No hay ninguna solicitud activa para este usuario.")
    }

    /******************************************************
    JOB_OFFERS WILL HAVE THE FOLLOWING KEY PATTERN:
    JobOffer_ProfessionId_CustomerId_ProfessionalId_
     *******************************************************/

    private fun getJobOfferKey(professionId: Long, customerId: Long, professionalId: Long) : String =
        "JobOffer_${professionId}_${customerId}_${professionalId}_"

    fun getJobOffers(customerId : Long) : Set<CreateJobOfferDTO> {
        validateCustomerHasAJobRequest(customerId)
        val keyPattern = "JobOffer_*_${customerId}_*_"
        val jobOfferKeys = redisJobOfferStorage.keys(keyPattern)
        return redisJobOfferStorage.opsForValue().multiGet(jobOfferKeys)?.toSet() ?: emptySet()
    }

    fun offerJob(jobOffer : CreateJobOfferDTO) {
        val keyPattern = "JobOffer_*_*_${jobOffer.professionalId}_"
        val professionalHasActiveOffer = redisJobOfferStorage.keys(keyPattern).isNotEmpty()
        if(professionalHasActiveOffer)
            throw BusinessException("No puede realizar más de una oferta simultáneamente.")
        val key = getJobOfferKey(jobOffer.professionId, jobOffer.customerId, jobOffer.professionalId)
        redisJobOfferStorage.opsForValue().set(key,jobOffer)
    }

    fun removeJobOffer(professionId : Long, customerId: Long, professionalId: Long) {
        val key = getJobOfferKey(professionId, customerId, professionalId)
        redisJobOfferStorage.delete(key)
    }

    private fun removeAllJobOffers(professionId : Long, customerId: Long){
        val keyPattern = "JobOffer_${professionId}_${customerId}_*_"
        val jobOfferKeys = redisJobOfferStorage.keys(keyPattern)
        redisJobOfferStorage.delete(jobOfferKeys)
    }

    /******************************************************
        CHATS WILL HAVE THE FOLLOWING KEY PATTERN:
        Chat_CustomerId_ProfessionalId_JobId_
    *******************************************************/

    private fun getChatKey(jobId: Long) : String = "Chat_${jobId}_"

    fun sendChatMessage(senderIsCustomer: Boolean, message: MessageDTO) {
        val key = getChatKey(message.jobId)
        redisChatStorage.opsForList().rightPush(key, message.toRedisMessage(senderIsCustomer))
    }

    fun getChatMessages(jobId: Long) : List<RedisMessageDTO> {
        val key = getChatKey(jobId)
        return redisChatStorage.opsForList().range(key, 0, -1) ?: emptyList()
    }

    fun deleteChatMessages(jobId: Long) {
        val key = getChatKey(jobId)
        redisChatStorage.delete(key)
    }
}