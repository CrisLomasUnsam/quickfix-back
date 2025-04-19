package quickfix.services

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import quickfix.dto.job.JobOfferDTO
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.message.ChatMessageDTO
import quickfix.dto.message.RedisMessageDTO
import quickfix.dto.message.toRedisMessage
import quickfix.utils.exceptions.BusinessException

@Service
class RedisService(
    private val redisJobRequestStorage: RedisTemplate<String, JobRequestDTO>,
    private val redisJobOfferStorage: RedisTemplate<String, JobOfferDTO>,
    private val redisChatStorage: RedisTemplate<String, RedisMessageDTO>
) {

    /******************************************************
    JOB_REQUESTS WILL HAVE THE FOLLOWING KEY PATTERN:
    JobRequest_ProfessionId_CustomerId_
     *******************************************************/


    private fun getJobRequestKey(professionId: Long, customerId: Long) : String =
        "JobRequest_${professionId}_${customerId}_"

    fun requestJob(jobRequest : JobRequestDTO) {
        val professionId = jobRequest.professionId
        val customerId = jobRequest.customerId
        val tempKey = "JobRequest_*_${customerId}_"
        val userHasPreviousRequest = redisJobRequestStorage.keys(tempKey).isNotEmpty()

        if(userHasPreviousRequest)
            throw BusinessException("Este usuario ya tiene una solicitud activa.")

        val key = getJobRequestKey(professionId, customerId)
        redisJobRequestStorage.opsForList().rightPush(key,jobRequest)
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
        redisJobRequestStorage.delete(key)
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

    fun getJobOffers(customerId : Long) : Set<JobOfferDTO> {
        validateCustomerHasAJobRequest(customerId)
        //TODO: Validar professionId? se supone que un usuario no va a tener más de un request activo
        val keyPattern = "JobOffer_*_${customerId}_*_"
        val jobOfferKeys = redisJobOfferStorage.keys(keyPattern)
        return redisJobOfferStorage.opsForValue().multiGet(jobOfferKeys)?.toSet() ?: emptySet()
    }

    fun offerJob(jobOffer : JobOfferDTO) {
        val keyPattern = "JobOffer_*_*_${jobOffer.professionalId}_"
        val professionalHasActiveOffer = redisJobOfferStorage.keys(keyPattern).isNotEmpty()
        if(professionalHasActiveOffer)
            throw BusinessException("No puede realizar más de una oferta simultáneamente.")
        val key = getJobOfferKey(jobOffer.professionId, jobOffer.professionalId, jobOffer.customerId)
        redisJobOfferStorage.opsForList().rightPush(key,jobOffer)
    }

    fun removeJobOffer(professionId : Long, customerId: Long, professionalId: Long) {
        val key = getJobOfferKey(professionId, customerId, professionalId)
        redisJobOfferStorage.delete(key)
    }

    /******************************************************
        CHATS WILL HAVE THE FOLLOWING KEY PATTERN:
        Chat_CustomerId_ProfessionalId_JobId_
    *******************************************************/

    private fun getChatKey(customerId: Long, professionalId: Long, jobId: Long) : String =
        "Chat_${customerId}_${professionalId}_${jobId}_"

    fun sendChatMessage(message: ChatMessageDTO) {
        val key = getChatKey(message.customerId, message.professionalId, message.jobId)
        redisChatStorage.opsForList().rightPush(key, message.toRedisMessage())
    }

    fun getChatMessages(customerId: Long, professionalId: Long, jobId: Long) : List<RedisMessageDTO> {
        val key = getChatKey(customerId, professionalId, jobId)
        return redisChatStorage.opsForList().range(key, 0, -1) ?: emptyList()
    }

    fun deleteChatMessages(customerId: Long, professionalId: Long, jobId: Long) {
        val key = getChatKey(customerId, professionalId, jobId)
        redisChatStorage.delete(key)
    }
}