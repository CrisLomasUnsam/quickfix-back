package quickfix.dao

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.User

@Component
interface UserRepository: CrudRepository<User, Long>{

    @EntityGraph(attributePaths = ["professionalInfo.professions"])
    fun findUserProfessionsById(id: Long): User?
  
    fun findByMail(mail: String): User?
  
    fun findByDni(dni: Int): User?
}