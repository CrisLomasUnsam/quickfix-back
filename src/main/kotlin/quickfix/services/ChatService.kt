package quickfix.services

import quickfix.dto.message.ChatMessageDTO
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import quickfix.dto.message.RedisMessageDTO
import quickfix.dto.message.toRedisMessage

@Service
class ChatService(
    private val redisTemplate: RedisTemplate<String, RedisMessageDTO>
) {

    /******************************************************
        CHATS WILL HAVE THE FOLLOWING KEY PATTERN:
        Chat_UserFromId_UserToId_JobId
    *******************************************************/

    private fun getChatKey(userFromId: Long, userToId: Long, jobId: Long) : String =
        "Chat_${userFromId}_${userToId}_${jobId}"

    fun sendMessage(message: ChatMessageDTO) {
        val key = getChatKey(message.userFromId, message.userToId, message.jobId)
        redisTemplate.opsForList().rightPush(key, message.toRedisMessage())
    }

    fun getMessages(userFromId: Long, userToId: Long, jobId: Long) : List<RedisMessageDTO> {
        val key = getChatKey(userFromId, userToId, jobId)
        return redisTemplate.opsForList().range(key, 0, -1) ?: emptyList()
    }
}