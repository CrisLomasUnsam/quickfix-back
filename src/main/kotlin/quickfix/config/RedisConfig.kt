package quickfix.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import quickfix.dto.chat.RedisMessageDTO
import quickfix.dto.job.jobOffer.CreateJobOfferDTO
import quickfix.dto.job.jobRequest.JobRequestDTO

@Configuration
class RedisConfig {

    @Bean
    fun redisChatStorage(connectionFactory: RedisConnectionFactory): RedisTemplate<String, RedisMessageDTO> {
        val storage = RedisTemplate<String, RedisMessageDTO>()
        storage.connectionFactory = connectionFactory
        storage.keySerializer = StringRedisSerializer()
        val objectMapper = ObjectMapper().apply {
            deactivateDefaultTyping()
        }
        val jacksonSerializer = Jackson2JsonRedisSerializer(objectMapper, RedisMessageDTO::class.java)
        storage.valueSerializer = jacksonSerializer
        return storage
    }

    @Bean
    fun redisJobRequestStorage(connectionFactory: RedisConnectionFactory): RedisTemplate<String, JobRequestDTO> {
        val storage = RedisTemplate<String, JobRequestDTO>()
        storage.connectionFactory = connectionFactory
        storage.keySerializer = StringRedisSerializer()
        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())
            deactivateDefaultTyping()
        }
        val jacksonSerializer = Jackson2JsonRedisSerializer(objectMapper, JobRequestDTO::class.java)
        storage.valueSerializer = jacksonSerializer
        return storage
    }

    @Bean
    fun redisJobOfferStorage(connectionFactory: RedisConnectionFactory): RedisTemplate<String, CreateJobOfferDTO> {
        val storage = RedisTemplate<String, CreateJobOfferDTO>()
        storage.connectionFactory = connectionFactory
        storage.keySerializer = StringRedisSerializer()
        val objectMapper = ObjectMapper().apply {
            deactivateDefaultTyping()
        }
        val jacksonSerializer = Jackson2JsonRedisSerializer(objectMapper, CreateJobOfferDTO::class.java)
        storage.valueSerializer = jacksonSerializer
        return storage
    }
}