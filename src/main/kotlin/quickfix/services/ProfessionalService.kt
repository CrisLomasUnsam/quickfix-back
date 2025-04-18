package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dto.job.CancelOfferJobDTO
import quickfix.dto.job.JobOfferDTO
import quickfix.dto.job.JobRequestDTO

@Service
class ProfessionalService(
    val redisService: RedisService,
    val userService: UserService
)  {

    private fun getProfessionIds(professionalId : Long) : Set<Long> {
        val professional = userService.getProfessionalInfo(professionalId)
        return professional.professions.map { it.id }.toSet()
    }

    fun getJobRequests(professionalId : Long) : Set<JobRequestDTO> {
        val professionIds = getProfessionIds(professionalId)
        return redisService.getJobRequests(professionIds)
    }

    fun offerJob(jobOffer : JobOfferDTO) =
        redisService.offerJob(jobOffer)

    fun cancelOfferJob(cancelOfferJob: CancelOfferJobDTO) =
        redisService.removeJobOffer(cancelOfferJob.professionId, cancelOfferJob.customerId, cancelOfferJob.professionalId)
}