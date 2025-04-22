package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.UserRepository
import quickfix.dto.job.jobRequest.CancelJobRequestDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.Profession
import quickfix.models.ProfessionalInfo
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val redisService: RedisService,
    private val professionService: ProfessionService,

) {
    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow{ BusinessException("Usuario no encontrado") }

    fun getExistById(id: Long) {
        require(userRepository.existsById(id)) {
            "No existe el user ${id}"
        }
    }
    @Transactional(rollbackFor = [Exception::class])
    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {

        val user = this.getUserById(id)
        user.updateUserInfo(modifiedInfo)

        modifiedInfo.address?.let { addressDTO ->
            user.address.updateAddressInfo(addressDTO)
        }
    }

    fun getProfessionsByUserId(id : Long) : Set<Profession> =
        userRepository.findUserProfessionsById(id)?.professionalInfo!!.professions

    fun requestJob(jobRequest : JobRequestDTO) {
        this.getExistById(jobRequest.customerId)
        val profession = professionService.getProfessionById(jobRequest.professionId)
        val updatedJobRequest = jobRequest.copy(professionId = profession.id)
        redisService.requestJob(updatedJobRequest)
    }


    fun getJobOffers(customerId : Long) =
        redisService.getJobOffers(customerId)

    fun cancelJobRequest (cancelJobRequest : CancelJobRequestDTO) {
        val (customerId, professionId) = cancelJobRequest
        this.getExistById(customerId)
        professionService.getProfessionById(professionId)
        redisService.removeJobRequest(customerId, professionId)
    }
}