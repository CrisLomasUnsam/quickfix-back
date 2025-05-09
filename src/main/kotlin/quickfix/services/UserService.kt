package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import quickfix.dao.UserRepository
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.Profession
import quickfix.models.ProfessionalInfo
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException
import java.util.Base64

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow{ BusinessException("Usuario no encontrado $id") }

    fun getUserByMail(mail: String): User =
        userRepository.findByMail(mail).orElseThrow{ BusinessException("Usuario no encontrado $mail") }

    fun assertUserExists(id: Long) {
        if (!userRepository.existsById(id))
            throw BusinessException("Usuario no encontrado: $id")
    }

    fun getProfessionalInfo(userId: Long) : ProfessionalInfo =
        userRepository.findUserWithProfessionalInfoById(userId).orElseThrow{ BusinessException() }.professionalInfo

    fun getActiveProfessionsByUserId(id: Long): Set<Profession> =
        getProfessionalInfo(id)
            .professionalProfessions
            .filter { it.active }
            .map { it.profession }
            .toSet()

    @Transactional(rollbackFor = [Exception::class])
    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = this.getUserById(id)
        user.updateUserInfo(modifiedInfo)
    }
    @Transactional(rollbackFor = [Exception::class])
    fun updateAvatar(currentUserId: Long, file: MultipartFile) {
        val user = this.getUserById(currentUserId)
        user.avatar = file.bytes
    }
    fun getAvatar(userId: Long): ByteArray = this.getUserById(userId).avatar
}