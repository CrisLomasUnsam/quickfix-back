package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dto.job.jobOffer.CancelJobOfferDTO
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import org.springframework.transaction.annotation.Transactional
import quickfix.dto.professional.FinancesDTO
import quickfix.dto.professional.NewCertificateDTO
import quickfix.models.Certificate
import quickfix.models.Profession
import quickfix.models.ProfessionalInfo
import quickfix.utils.exceptions.BusinessException
import java.sql.SQLException

@Service
class ProfessionalService(
    val redisService: RedisService,
    val userService: UserService,
    private val professionService: ProfessionService
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

    fun getFinances(professionalId: Long): FinancesDTO {
        val professional = userService.getUserById(professionalId)
        val financesDTO = FinancesDTO(
            balance = professional.professionalInfo.balance,
            debt = professional.professionalInfo.debt
        )
        return financesDTO
    }

    fun getProfessions(proffesionalId: Long) : List<Profession> {
        val professionIds : Set<Long> = this.getProfessionIds(proffesionalId)
        return professionIds.map { professionService.getProfessionById(it) }
    }

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun addProfession(professionalId: Long, professionName: String) {
        val professional = userService.getUserById(professionalId)
        val profession = professionService.getByNameIgnoreCase(professionName)

        if (professional.professionalInfo.hasProfession(profession.id))
            throw BusinessException("La profesión ya forma parte de sus servicios")

        professional.professionalInfo.addProfession(profession)
    }

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun deleteProfession(professionalId: Long, professionName: String) {
        val professional = userService.getUserById(professionalId)
        val professionId = professionService.getByNameIgnoreCase(professionName).id

        if (!professional.professionalInfo.hasProfession(professionId))
            throw BusinessException("La profesión no forma parte de sus servicios")

        professional.professionalInfo.removeProfession(professionId)
    }

    @Transactional(readOnly = true)
    fun getCertificates(professionalId: Long): Set<Certificate> {
       val professional = userService.getUserById(professionalId)
       return professional.professionalInfo.certificates.toSet()
    }

    @Transactional(rollbackFor = [Exception::class])
    fun addCertificate(professionalId: Long, newCert: NewCertificateDTO) {
        val professionalInfo : ProfessionalInfo = userService.getProfessionalInfo(professionalId)
        val profession = professionService.getProfessionById(newCert.professionId)
        professionalInfo.validateCertificateAlreadyExists(newCert.name)

        val newCertificate = Certificate().apply {
            this.name = newCert.name
            this.profession = profession;
            this.img = newCert.img.trim()
        }
        professionalInfo.addCertificate(newCertificate)
    }

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun deleteCertificate(professionalId: Long, certificateName: String) {
        val professionalInfo = userService.getProfessionalInfo(professionalId)
        professionalInfo.deleteCertificate(certificateName)
    }
}