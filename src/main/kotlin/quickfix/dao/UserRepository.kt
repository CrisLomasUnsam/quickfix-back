package quickfix.dao

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.User
import java.util.Optional

@Component
interface UserRepository: CrudRepository<User, Long>{

    fun findByMail(mail: String): User? = this.findAll().find { it.mail == mail }

    fun findByDni(dni: Int): Optional<User>

    @EntityGraph(attributePaths = ["professionalInfo.professions"])
    fun findUserProfessionsById(id: Long): User?
}