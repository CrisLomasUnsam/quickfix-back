package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.UserRepository
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.ProfessionalInfo
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val redisService: RedisService
) {

    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow{throw BusinessException()}

    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = getUserById(id) ?: throw BusinessException()
        user.updateUserInfo(modifiedInfo)
    }

    fun getProfessionalInfo(id : Long) : ProfessionalInfo =
        userRepository.findById(id).orElseThrow{throw BusinessException()}.professionalInfo

    fun requestJob(jobRequest : JobRequestDTO) =
        redisService.requestJob(jobRequest)

    fun getJobOffers(customerId : Long) =
        redisService.getJobOffers(customerId)


}