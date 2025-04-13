package quickfix.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import quickfix.dto.message.RedisMessageDTO

@Configuration
class RedisConfig {

    @Bean
    fun redisStorage(connectionFactory: RedisConnectionFactory): RedisTemplate<String, RedisMessageDTO> {
        val storage = RedisTemplate<String, RedisMessageDTO>()
        storage.connectionFactory = connectionFactory
        storage.keySerializer = StringRedisSerializer()
        storage.valueSerializer = GenericJackson2JsonRedisSerializer()
        return storage
    }
}