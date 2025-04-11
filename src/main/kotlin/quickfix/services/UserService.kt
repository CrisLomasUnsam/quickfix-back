package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.JobRepository
import quickfix.dao.UserRepository
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.Job
import quickfix.models.User
import quickfix.utils.JobSearchParameters
import quickfix.utils.exceptions.BusinessException

@Service
class UserService (
    private val userRepository: UserRepository
) {

    fun getUserInfoById(id: Long): User? =
        userRepository.getById(id)

    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = getUserInfoById(id) ?: throw BusinessException("Información no encontrada")
        user.updateUserInfo(modifiedInfo)
    }

    fun postJobRequest(jobRequest: JobRequestDTO) {
        // Este service mete ese jobRequest en una base no relacional, como redis o DB
    }


}