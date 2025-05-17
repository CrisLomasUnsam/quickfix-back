package quickfix.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.Profession
import java.util.*

@Component
interface ProfessionRepository: CrudRepository<Profession, Long> {
  
    fun findByNameIgnoreCase(name: String): Optional<Profession>
}