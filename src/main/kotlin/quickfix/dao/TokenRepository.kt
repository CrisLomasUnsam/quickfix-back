package quickfix.dao

import org.springframework.data.repository.CrudRepository
import quickfix.models.Token

interface TokenRepository: CrudRepository<Token, Long> {
    fun findByValue(token: String): Token?
}