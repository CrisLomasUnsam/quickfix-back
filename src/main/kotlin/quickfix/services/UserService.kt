package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.ProfessionRepository
import quickfix.dao.UserRepository
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.ProfessionalInfo
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val redisService: RedisService,
    private val professionRepository : ProfessionRepository
) {

    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow{ BusinessException("Usuario no encontrado") }

    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = getUserById(id)
        user.updateUserInfo(modifiedInfo)
    }

    fun getProfessionalInfo(id : Long) : ProfessionalInfo =
        this.getUserById(id).professionalInfo

    fun requestJob(jobRequest : JobRequestDTO) {

         userRepository.findById(jobRequest.customerId)
            .orElseThrow { BusinessException("El cliente con id ${jobRequest.customerId} no existe.") }


        val profession = professionRepository.findByName(jobRequest.profession)
            .orElseThrow { BusinessException("la profession con id ${jobRequest.professionId} no existe.") }


        val updatedJobRequest = jobRequest.copy(professionId = profession.id)

        redisService.requestJob(updatedJobRequest)
    }


    fun getJobOffers(customerId : Long) =
        redisService.getJobOffers(customerId)

    fun cancelJobRequest (professionId : Long, customerId : Long) {

        userRepository.findById(customerId)
            .orElseThrow { BusinessException("El cliente con id $customerId no existe.") }


        professionRepository.findById(professionId)
            .orElseThrow { BusinessException("La profesión con id $professionId no existe.") }

        redisService.removeJobRequest(professionId, customerId)
    }
}