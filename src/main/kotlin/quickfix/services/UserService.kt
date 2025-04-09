package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.UserRepository
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException

@Service
class UserService (
    val userRepository: UserRepository
) {

    fun getUserInfoById(id: Long): User? =
        userRepository.getById(id)

    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = getUserInfoById(id) ?: throw BusinessException("Información no encontrada")
        user.updateUserInfo(modifiedInfo)
    }
}