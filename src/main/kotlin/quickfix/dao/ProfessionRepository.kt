package quickfix.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.Profession
import java.util.Optional

@Component
interface ProfessionRepository: CrudRepository<Profession, Long> {
    fun findByName(name: String): Optional<Profession>
}