package quickfix.dao

import org.springframework.stereotype.Component
import quickfix.models.User

@Component
class UserRepository: Repository<User>() {

    fun findByMail(mail: String): User? =
        this.elements.find { it.mail == mail }
}