package quickfix.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import quickfix.dto.chat.RedisMessageDTO
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobRequest.ProfessionalJobRequestDTO

@Configuration
class RedisConfig {

    @Bean
    fun redisChatStorage(connectionFactory: RedisConnectionFactory): RedisTemplate<String, RedisMessageDTO> {
        val storage = RedisTemplate<String, RedisMessageDTO>()
        storage.connectionFactory = connectionFactory
        storage.keySerializer = StringRedisSerializer()
        val objectMapper = ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
            deactivateDefaultTyping()
        }
        val jacksonSerializer = Jackson2JsonRedisSerializer(objectMapper, RedisMessageDTO::class.java)
        storage.valueSerializer = jacksonSerializer
        return storage
    }

    @Bean
    fun redisJobRequestStorage(connectionFactory: RedisConnectionFactory): RedisTemplate<String, ProfessionalJobRequestDTO> {
        val storage = RedisTemplate<String, ProfessionalJobRequestDTO>()
        storage.connectionFactory = connectionFactory
        storage.keySerializer = StringRedisSerializer()
        val objectMapper = ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
            registerModule(JavaTimeModule())
            deactivateDefaultTyping()
        }
        val jacksonSerializer = Jackson2JsonRedisSerializer(objectMapper, ProfessionalJobRequestDTO::class.java)
        storage.valueSerializer = jacksonSerializer
        return storage
    }

    @Bean
    fun redisJobOfferStorage(connectionFactory: RedisConnectionFactory): RedisTemplate<String, JobOfferDTO> {
        val storage = RedisTemplate<String, JobOfferDTO>()
        storage.connectionFactory = connectionFactory
        storage.keySerializer = StringRedisSerializer()
        val objectMapper = ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
            registerModule(JavaTimeModule())
            deactivateDefaultTyping()
        }
        val jacksonSerializer = Jackson2JsonRedisSerializer(objectMapper, JobOfferDTO::class.java)
        storage.valueSerializer = jacksonSerializer
        return storage
    }
}