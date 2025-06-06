package quickfix.services

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import quickfix.dto.chat.MessageDTO
import quickfix.dto.chat.RedisMessageDTO
import quickfix.dto.chat.toRedisMessage
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.utils.INSTANT_REQUEST_LIVE_DAYS
import quickfix.utils.MAX_CUSTOMER_REQUESTS_AT_TIME
import quickfix.utils.exceptions.JobException
import quickfix.utils.jobs.*
import java.time.Duration

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

    fun requestJob(jobRequest : JobRequestDTO) {

        val customerId = jobRequest.customer.id
        assertCustomerCanCreateAJobRequest(customerId)

        val key = getJobRequestKey(professionId = jobRequest.professionId, customerId = customerId)
        assertKeyDoesNotExist(key)

        redisJobRequestStorage.opsForValue().set(key, jobRequest)
        //If it is a future request, we set a TTL to cancel it automatically as soon as the date and time of need for the service arrives.
        val ttl = getJobRequestTTLForRedis(jobRequest)
        redisJobRequestStorage.expire(key, ttl)
    }

    fun getMyJobRequests(customerId: Long) : List<JobRequestDTO> {
        val requestsKeys = redisJobRequestStorage.keys("JobRequest_*_${customerId}_")
        val myJobRequests = redisJobRequestStorage.opsForValue().multiGet(requestsKeys) ?: emptySet()
        return myJobRequests.toList().sortedWith( compareByDescending<JobRequestDTO>{it.instantRequest}.thenBy{ it.neededDatetime }.thenBy { it.professionId })
    }

    fun countOffersForRequest(jobRequest: JobRequestDTO) : Int {
        val customerId = jobRequest.customer.id
        val professionId = jobRequest.professionId
        val keyPattern = "JobOffer_${professionId}_${customerId}_*_"
        return redisJobRequestStorage.keys(keyPattern).size
    }

    fun getJobRequests(activeProfessionIds : Set<Long>, excludeOfferedKeys : Set<String>) : List<JobRequestDTO> {
        val jobRequests = mutableListOf<JobRequestDTO>()
        activeProfessionIds.forEach { professionId ->
            val tempKey = "JobRequest_${professionId}_*_"
            val requestsKeys = redisJobRequestStorage.keys(tempKey) - excludeOfferedKeys
            val requestsValues = redisJobRequestStorage.opsForValue().multiGet(requestsKeys) ?: emptySet()
            jobRequests.addAll(requestsValues)
        }
        return jobRequests.sortedWith( compareByDescending<JobRequestDTO> {it.neededDatetime}.thenBy { it.professionId }.thenBy { it.customer.averageRating })
    }

    fun getJobRequest(jobRequestId: String) : JobRequestDTO? {
        return redisJobRequestStorage.opsForValue().get(jobRequestId)
    }

    fun removeJobRequest(professionId : Long, customerId: Long) {
        val key = getJobRequestKey(professionId, customerId)
        println("Key: ${key}")
        assertCustomerHasJobRequest(professionId, customerId)
        redisJobRequestStorage.delete(key)
        this.removeAllJobOffers(professionId, customerId)
    }

    private fun assertCustomerCanCreateAJobRequest(customerId: Long) {
        val tempKey = "JobRequest_*_${customerId}_"
        val userRequestsCount = redisJobRequestStorage.keys(tempKey).size

        if (userRequestsCount >= MAX_CUSTOMER_REQUESTS_AT_TIME)
            throw JobException("Solo es posible tener hasta un máximo de $MAX_CUSTOMER_REQUESTS_AT_TIME solicitudes activas al mismo tiempo.")
    }

    private fun assertKeyDoesNotExist(key: String){
        if(redisJobRequestStorage.hasKey(key))
            throw JobException("No es posible tener dos solicitudes activas de una misma categoría. Ya tiene una solicitud para esta categoría.")
    }

    private fun assertCustomerHasJobRequest(professionId: Long, customerId : Long){
        if(!redisJobRequestStorage.hasKey(getJobRequestKey(professionId, customerId)))
            throw JobException("La solicitud ha expirado.")
    }
    /* CLEANUP */

    fun cleanupJobRequestsForTesting() {
        val keys = redisJobRequestStorage.keys("JobRequest_*_*_")
        keys.forEach { key ->
            redisJobRequestStorage.delete(key)
        }
    }


    /******************************************************
    JOB_OFFERS WILL HAVE THE FOLLOWING KEY PATTERN:
    JobOffer_ProfessionId_CustomerId_ProfessionalId_
     *******************************************************/

    fun getJobOffers(professionId: Long, customerId : Long, ) : List<JobOfferDTO> {
        assertCustomerHasJobRequest(professionId, customerId)
        val keyPattern = "JobOffer_${professionId}_${customerId}_*_"
        return getSortedJobOffers(keyPattern)
    }

    fun getMyJobOffers(professionalId : Long) : List<JobOfferDTO> {
        val keyPattern = "JobOffer_*_*_${professionalId}_"
        return getSortedJobOffers(keyPattern)
    }

    private fun getSortedJobOffers(keyPattern: String) : List<JobOfferDTO> {
        val jobOfferKeys = redisJobOfferStorage.keys(keyPattern)
        val offers = redisJobOfferStorage.opsForValue().multiGet(jobOfferKeys)?.toList() ?: emptySet()
        return offers.sortedWith( compareByDescending<JobOfferDTO> {it.request.neededDatetime}.thenBy{ it.request.professionId }.thenBy {it.distance})
    }

    fun offerJob(jobOffer : JobOfferDTO) {
        assertJobOffersDoNotCollide(jobOffer)
        val request = jobOffer.request
        val key = getJobOfferKey(request.professionId, request.customer.id, jobOffer.professional.id)
        redisJobOfferStorage.opsForValue().set(key,jobOffer)
        //If it is a future request, we set a TTL to cancel it automatically as soon as the date and time of need for the service arrives.
        val ttl = getJobOfferTTLForRedis(jobOffer)
        redisJobRequestStorage.expire(key, ttl)
    }

    private fun assertJobOffersDoNotCollide(newOffer : JobOfferDTO){
        val keyPattern = "JobOffer_*_*_${newOffer.professional.id}_"
        val activeOffersKeys = redisJobOfferStorage.keys(keyPattern)
        val activeOffers = redisJobOfferStorage.opsForValue().multiGet(activeOffersKeys)?.toList() ?: emptySet()
        val newOfferEndDatetime = getJobOfferEndtime(newOffer)

        activeOffers.forEach { activeOffer ->
            val activeOfferStartDatetime = activeOffer.request.neededDatetime
            val activeOfferEndDatetime = getJobOfferEndtime(activeOffer)
            if(dateTimesCollides(newOffer.request.neededDatetime, newOfferEndDatetime, activeOfferStartDatetime, activeOfferEndDatetime))
                throw JobException("Usted ya tiene una oferta activa en este rango de tiempo.")
        }
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

    /* CLEANUP */

    fun cleanupJobOffersForTesting() {
        val keys = redisJobRequestStorage.keys("JobOffer_*_*_*_")
        keys.forEach { key -> redisJobRequestStorage.delete(key) }
    }

    /******************************************************
        CHATS WILL HAVE THE FOLLOWING KEY PATTERN:
        Chat_CustomerId_ProfessionalId_JobId_
    *******************************************************/

    private fun getChatKey(jobId: Long) : String = "Chat_${jobId}_"

    fun sendChatMessage(senderIsCustomer: Boolean, message: MessageDTO) {
        val key = getChatKey(message.jobId)
        val hasToSetTTL = !redisChatStorage.hasKey(key)
        redisChatStorage.opsForList().rightPush(key, message.toRedisMessage(senderIsCustomer))
        if(hasToSetTTL)
            redisChatStorage.expire(key, Duration.ofDays(INSTANT_REQUEST_LIVE_DAYS))
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