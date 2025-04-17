package quickfix.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.Profession

@Component
interface ProfessionRepository: CrudRepository<Profession, Long> {
    fun findByNameContainingIgnoreCase(name: String): Profession?
}