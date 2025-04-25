package quickfix.dao

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.ProfessionalInfo
import quickfix.models.User
import java.util.*

@Component
interface UserRepository: CrudRepository<User, Long>{

    @EntityGraph(attributePaths = ["professionalInfo.professions"])
    fun findUserProfessionsById(id: Long): Optional<User>

    @EntityGraph(attributePaths = ["professionalInfo", "professionalInfo.professions", "professionalInfo.certificates"])
    fun findUserWithProfessionalInfoById(id: Long): Optional<User>

    fun findByMail(mail: String): User?
  
    fun findByDni(dni: Int): User?
}