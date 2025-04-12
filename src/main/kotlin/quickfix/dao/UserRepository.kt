package quickfix.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.User

@Component
interface UserRepository: CrudRepository<User, Long>{

    fun findByMail(mail: String): User? = this.findAll().find { it.mail == mail }
}