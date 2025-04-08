package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.UserInfoRepository
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.UserInfo
import quickfix.utils.exceptions.BusinessException

@Service
class UserService (
    val userInfoRepository: UserInfoRepository
) {

    fun getUserInfoById(id: Long): UserInfo? =
        userInfoRepository.getById(id)

    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val userInfo = getUserInfoById(id) ?: throw BusinessException("Información no encontrada")
        userInfo.updateUserInfo(modifiedInfo)
    }
}