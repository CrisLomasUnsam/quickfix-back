package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.UserRepository
import quickfix.models.User

@Service
class UserService (
    val userRepository: UserRepository
) {
    fun getUserInfoById(id: Long): User? = userRepository.getById(id)
}