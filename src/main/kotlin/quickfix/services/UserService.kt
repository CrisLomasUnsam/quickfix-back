package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.UserRepository
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException

@Service
class UserService (
    private val userRepository: UserRepository
) {

    fun getUserInfoById(id: Long): User? =
        userRepository.findById(id).orElseThrow{throw BusinessException()}

    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = getUserInfoById(id) ?: throw BusinessException()
        user.updateUserInfo(modifiedInfo)
    }

    fun postJobRequest(jobRequest: JobRequestDTO) {
        // Este service mete ese jobRequest en una base no relacional, como redis o DB
    }


}