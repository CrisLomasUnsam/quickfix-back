package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.CustomerRepository
import quickfix.dao.ProfessionalRepository
import quickfix.dao.UserInfoRepository
import quickfix.models.User
import quickfix.models.UserInfo
import quickfix.utils.exceptions.BusinessException

@Service
class UserService (
    val userInfoRepository: UserInfoRepository
) {

    fun getUserInfoById(id: Long): UserInfo? =
        userInfoRepository.getById(id)
}