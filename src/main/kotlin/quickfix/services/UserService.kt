package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.CustomerRepository
import quickfix.dao.ProfessionalRepository
import quickfix.models.User

@Service
class UserService (
    val professionalRepository: ProfessionalRepository,
    val customerRepository: CustomerRepository
) {
    fun createUser(user: User): Any {
            TODO("Not yet implemented")
    }

    fun getUserById(id: Int): Any {
            TODO("Not yet implemented")
    }

}