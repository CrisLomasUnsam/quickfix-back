package quickfix.dao

import org.springframework.data.repository.CrudRepository
import quickfix.models.RegisterToken

interface TokenRepository: CrudRepository<RegisterToken, Long> {
    fun findByToken(token: String): RegisterToken?
}