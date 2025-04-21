package quickfix.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import quickfix.dto.job.jobOffer.CancelJobOfferDTO
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobRequest.JobRequestDTO

@Service
class ProfessionalService(
    val redisService: RedisService,
    val userService: UserService
)  {


    private fun getProfessionIds(professionalId : Long) : Set<Long> {
        val professions = userService.getProfessionsByUserId(professionalId)
        return professions.map { it.id }.toSet()
    }

    fun getJobRequests(professionalId : Long) : Set<JobRequestDTO> {
        val professionIds : Set<Long> = getProfessionIds(professionalId)
        return redisService.getJobRequests(professionIds)
    }

    fun offerJob(jobOffer : JobOfferDTO) =
        redisService.offerJob(jobOffer)

    fun cancelJobOffer(cancelOfferJob: CancelJobOfferDTO) =
        redisService.removeJobOffer(cancelOfferJob.professionId, cancelOfferJob.customerId, cancelOfferJob.professionalId)
}