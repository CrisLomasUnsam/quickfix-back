package quickfix.services

import quickfix.dto.message.ChatMessageDTO
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import quickfix.dto.message.RedisMessageDTO
import quickfix.dto.message.toRedisMessage

@Service
class ChatService(
    private val redisStorage: RedisTemplate<String, RedisMessageDTO>
) {

    /******************************************************
        CHATS WILL HAVE THE FOLLOWING KEY PATTERN:
        Chat_CustomerId_ProfessionalId_JobId
    *******************************************************/

    private fun getChatKey(customerId: Long, professionalId: Long, jobId: Long) : String =
        "Chat_${customerId}_${professionalId}_${jobId}"

    fun sendMessage(message: ChatMessageDTO) {
        val key = getChatKey(message.customerId, message.professionalId, message.jobId)
        redisStorage.opsForList().rightPush(key, message.toRedisMessage())
    }

    fun getMessages(customerId: Long, professionalId: Long, jobId: Long) : List<RedisMessageDTO> {
        val key = getChatKey(customerId, professionalId, jobId)
        return redisStorage.opsForList().range(key, 0, -1) ?: emptyList()
    }

    fun deleteMessages(customerId: Long, professionalId: Long, jobId: Long) {
        val key = getChatKey(customerId, professionalId, jobId)
        redisStorage.delete(key)
    }
}