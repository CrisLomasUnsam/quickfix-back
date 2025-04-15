package quickfix.dao

import org.springframework.data.repository.CrudRepository
import quickfix.models.Profession

interface ProfessionRepository: CrudRepository<Profession, Long> {

    fun findByNameContainingIgnoreCase(name: String): Profession
}
