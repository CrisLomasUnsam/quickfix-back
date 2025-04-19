package quickfix.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.Profession
import quickfix.utils.enums.ProfessionTypes

@Component
interface ProfessionRepository: CrudRepository<Profession, Long> {
    fun findByProfessionType(type: ProfessionTypes): Profession?
}