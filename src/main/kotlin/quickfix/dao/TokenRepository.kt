package quickfix.dao

import org.springframework.data.repository.CrudRepository
import quickfix.models.Token
import quickfix.models.User

interface TokenRepository: CrudRepository<Token, Long> {
    fun findByValue(token: String): Token?
    fun getTokensByUser(user: User): Token
}