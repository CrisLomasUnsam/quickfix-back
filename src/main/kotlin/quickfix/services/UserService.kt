package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.UserRepository
import quickfix.dto.job.AcceptedJobOfferDTO
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.job.toDTO
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
    private val jobService : JobService,

) {

    fun getUserById(id: Long): User =
        userRepository.findById(id).orElseThrow{ BusinessException("Usuario no encontrado") }


    @Transactional(rollbackFor = [Exception::class])
    fun changeUserInfo(id: Long, modifiedInfo: UserModifiedInfoDTO) {

        val user = this.getUserById(id)
        user.updateUserInfo(modifiedInfo)

        modifiedInfo.address?.let { addressDTO ->
            user.address.updateAddressInfo(addressDTO)
        }
    }


    fun getProfessionalInfo(id : Long) : ProfessionalInfo =
        this.getUserById(id).professionalInfo

    fun requestJob(jobRequest : JobRequestDTO) {

         userRepository.findById(jobRequest.customerId)
            .orElseThrow { BusinessException("El cliente con id ${jobRequest.customerId} no existe.") }


        val profession = professionService.getProfessionByName(jobRequest.profession)


        val updatedJobRequest = jobRequest.copy(professionId = profession.id)

        redisService.requestJob(updatedJobRequest)
    }


    fun getJobOffers(customerId : Long) =
        redisService.getJobOffers(customerId)

    fun cancelJobRequest (professionId : Long, customerId : Long) {

        userRepository.findById(customerId)
            .orElseThrow { BusinessException("El cliente con id $customerId no existe.") }

        professionService.getProfessionById(professionId)


        redisService.removeJobRequest(professionId, customerId)
    }
    @Transactional(rollbackFor = [Exception::class])
    fun acceptJobOffer(accepted: AcceptedJobOfferDTO) {

        val customer: User = this.getUserById(accepted.customerId)
        val professional : User = this.getUserById(accepted.professionalId)
        val profession: Profession = professionService.getProfessionByName(accepted.profession)


        val offerDTO = redisService.getJobOffers(accepted.customerId)
println()

        val offerProfessiona = offerDTO.firstOrNull { it.professionalId == accepted.professionalId }
            ?: throw BusinessException("…")


        jobService.createJob(offerProfessiona.toDTO( customer, professional, profession))

        //Limpia el request y offer me parecio conveniente borrarla inmpediatamente a pesar de q tenga el ttl
        redisService.removeJobRequest(profession.id, accepted.customerId)
        redisService.removeJobOffer(profession.id, accepted.customerId, accepted.professionalId)
    }
}