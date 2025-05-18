package quickfix.services

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import quickfix.dto.chat.MessageDTO
import quickfix.dto.chat.RedisMessageDTO
import quickfix.dto.chat.toRedisMessage
import quickfix.dto.job.jobOffer.CreateJobOfferDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.utils.INSTANT_REQUEST_LIVE_DAYS
import quickfix.utils.MAX_CUSTOMER_REQUESTS_AT_TIME
import quickfix.utils.exceptions.JobException
import java.time.Duration
import java.time.LocalDateTime

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

    fun requestJob(jobRequest : JobRequestDTO) {

        val customerId = jobRequest.customerId
        assertCustomerCanCreateAJobRequest(customerId)

        val key = getJobRequestKey(jobRequest.professionId, customerId)
        assertKeyDoesNotExist(key, "No es posible tener dos solicitudes activas de una misma categoría. Ya tiene una solicitud para la categoría: ${jobRequest.professionName}")

        redisJobRequestStorage.opsForValue().set(key,jobRequest)

        //If it is a future request, we set a TTL to cancel it automatically as soon as the date and time of need for the service arrives.
        val durationUntilRequestIsNeeded = Duration.between(LocalDateTime.now(), jobRequest.neededDatetime).abs()
        if(jobRequest.instantRequest)
            redisJobRequestStorage.expire(key, Duration.ofDays(INSTANT_REQUEST_LIVE_DAYS))
        else
            redisJobRequestStorage.expire(key, durationUntilRequestIsNeeded)
    }

    fun getMyJobRequests(customerId: Long) : List<JobRequestDTO> {
        val requestsKeys = redisJobRequestStorage.keys("JobRequest_*_${customerId}_")
        val myJobRequests = redisJobRequestStorage.opsForValue().multiGet(requestsKeys) ?: emptySet()
        return myJobRequests.toList().sortedBy { it.neededDatetime }
    }

    fun countOffersForRequest(jobRequest: JobRequestDTO) : Int {
        val customerId = jobRequest.customerId
        val professionId = jobRequest.professionId
        val keyPattern = "JobOffer_${professionId}_${customerId}_*_"
        return redisJobRequestStorage.keys(keyPattern).size
    }

    fun getJobRequests(activeProfessionIds : Set<Long>) : List<JobRequestDTO> {
        val jobRequests = mutableListOf<JobRequestDTO>()
        activeProfessionIds.forEach { professionId ->
            val tempKey = "JobRequest_${professionId}_*_"
            val requestsKeys = redisJobRequestStorage.keys(tempKey)
            val requestsValues = redisJobRequestStorage.opsForValue().multiGet(requestsKeys) ?: emptySet()
            jobRequests.addAll(requestsValues)
        }
        return jobRequests.sortedBy { it.neededDatetime }
    }

    fun removeJobRequest(professionId : Long, customerId: Long) {
        val key = getJobRequestKey(professionId, customerId)

        if (!redisJobRequestStorage.hasKey(key))
            throw JobException("No existe una solicitud activa para el usuario $customerId en la profesión $professionId")

        redisJobRequestStorage.delete(key)
        this.removeAllJobOffers(professionId, customerId)
    }

    private fun assertCustomerCanCreateAJobRequest(customerId: Long) {
        val tempKey = "JobRequest_*_${customerId}_"
        val userRequestsCount = redisJobRequestStorage.keys(tempKey).size

        if (userRequestsCount >= MAX_CUSTOMER_REQUESTS_AT_TIME)
            throw JobException("Solo es posible tener hasta un máximo de $MAX_CUSTOMER_REQUESTS_AT_TIME solicitudes activas al mismo tiempo.")
    }

    private fun assertKeyDoesNotExist(key: String, errorMessage: String){
        if(redisJobRequestStorage.hasKey(key))
            throw JobException(errorMessage)
    }

    private fun assertCustomerHasAJobRequest(customerId : Long){
        val tempKey = "JobRequest_*_${customerId}_"
        val requestsKeys = redisJobRequestStorage.keys(tempKey)
        if(requestsKeys.isEmpty())
            throw JobException("No hay ninguna solicitud activa para este usuario.")
    }
    /* CLEANUP */

    fun cleanupJobRequestsForTesting() {
        val keys = redisJobRequestStorage.keys("JobRequest_*_*")
        keys.forEach { key ->
            redisJobRequestStorage.delete(key)
        }
    }


    /******************************************************
    JOB_OFFERS WILL HAVE THE FOLLOWING KEY PATTERN:
    JobOffer_ProfessionId_CustomerId_ProfessionalId_
     *******************************************************/

    private fun getJobOfferKey(professionId: Long, customerId: Long, professionalId: Long) : String =
        "JobOffer_${professionId}_${customerId}_${professionalId}_"

    fun getJobOffers(customerId : Long) : Set<CreateJobOfferDTO> {
        assertCustomerHasAJobRequest(customerId)
        val keyPattern = "JobOffer_*_${customerId}_*_"
        val jobOfferKeys = redisJobOfferStorage.keys(keyPattern)
        return redisJobOfferStorage.opsForValue().multiGet(jobOfferKeys)?.toSet() ?: emptySet()
    }

    fun offerJob(jobOffer : CreateJobOfferDTO) {
        val keyPattern = "JobOffer_*_*_${jobOffer.professionalId}_"
        val professionalHasActiveOffer = redisJobOfferStorage.keys(keyPattern).isNotEmpty()
        if(professionalHasActiveOffer)
            throw JobException("No puede realizar más de una oferta simultáneamente.")
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