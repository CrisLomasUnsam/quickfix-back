package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.UserRepository
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.Profession
import quickfix.models.ProfessionalInfo
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow{ BusinessException("Usuario no encontrado $id") }

    fun assertUserExists(id: Long) {
        if (!userRepository.existsById(id))
            throw BusinessException("Usuario no encontrado: $id")
    }

    fun getProfessionalInfo(userId: Long) : ProfessionalInfo =
        userRepository.findUserWithProfessionalInfoById(userId).orElseThrow{ BusinessException() }.professionalInfo

    fun getProfessionsByUserId(id: Long): Set<Profession> =
        userRepository.findUserProfessionsById(id).orElseThrow { BusinessException() }.professionalInfo.professions

    @Transactional(rollbackFor = [Exception::class])
    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = this.getUserById(id)
        user.updateUserInfo(modifiedInfo)
    }

}