package quickfix.dao

import org.springframework.data.repository.CrudRepository
import quickfix.models.VerificationToken

interface TokenRepository: CrudRepository<VerificationToken, Long> {
    fun findByToken(token: String): VerificationToken?
}