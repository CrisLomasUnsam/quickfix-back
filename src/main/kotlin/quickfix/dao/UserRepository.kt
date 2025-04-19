package quickfix.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.User
import java.util.Optional

@Component
interface UserRepository: CrudRepository<User, Long>{

    fun findByMail(mail: String): User?
    fun findByDni(dni: Int): User?
}