package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.UserRepository
import quickfix.dto.job.CancelJobOfferDTO
import quickfix.dto.job.JobOfferDTO
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.professional.CertificateDTO
import quickfix.dto.professional.FinancesDTO
import quickfix.dto.professional.NewCertificateDTO
import quickfix.models.Certificate
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException
import java.sql.SQLException

@Service
class ProfessionalService(
    val redisService: RedisService,
    val userService: UserService,
    private val userRepository: UserRepository,
    private val professionService: ProfessionService
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

    fun cancelJobOffer(cancelOfferJob: CancelJobOfferDTO) =
        redisService.removeJobOffer(cancelOfferJob.professionId, cancelOfferJob.customerId, cancelOfferJob.professionalId)

    fun getFinances(professionalId: Long): FinancesDTO {
        val professional = userService.getUserById(professionalId)
        val financesDTO = FinancesDTO(
            balanceActual = professional.professionalInfo.balance,
            deudaActual = professional.professionalInfo.debt
        )
        return financesDTO
    }

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun addProfession(professionalId: Long, profession: String) {
        val professional = userService.getUserById(professionalId)
        val professionEntity = professionService.getByNameIgnoreCase(profession)
        if (professional.professionalInfo.professions.contains(professionEntity)) { throw BusinessException("La profesión ya forma parte de sus servicios") }
        professional.professionalInfo.professions.add(professionEntity)
    }

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun deleteProfession(professionalId: Long, profession: String) {
        val professional = userService.getUserById(professionalId)
        val professionEntity = professionService.getByNameIgnoreCase(profession)
        if (!professional.professionalInfo.professions.contains(professionEntity)) { throw BusinessException("La profesión no forma parte de sus servicios") }
        professional.professionalInfo.professions.remove(professionEntity)
        deleteAllCertificates(professional, profession)
    }

    private fun deleteAllCertificates(professional: User, profession: String) {
        val certificateMapEntity = professional.professionalInfo.certificates.find { it.profession.name.equals(profession, ignoreCase = true) } ?: throw BusinessException("No se han encontrado certificados asociados a esa profesión")
        professional.professionalInfo.certificates.remove(certificateMapEntity)
    }

    fun getCertificates(professionalId: Long): List<CertificateDTO> {
       val professional = userService.getUserById(professionalId)
       return professional.professionalInfo.certificates.map {
           CertificateDTO(
               profession = it.profession.name,
               imgs = it.imgs
           )
       }
    }

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun addCertificate(professionalId: Long, dto: NewCertificateDTO) {
        val professional = userService.getUserById(professionalId)
        val professionFromDto = dto.profession
        val professionEntity = professionService.getByNameIgnoreCase(professionFromDto)
        val certificateMappingExists = professional.professionalInfo.certificates.find { it.profession.name == professionEntity.name }
        if (certificateMappingExists != null) {
            certificateMappingExists.imgs.add(dto.img.trim())
        } else {
            val newCertificate = Certificate().apply { this.profession = professionEntity; this.imgs.add(dto.img.trim()) }
            professional.professionalInfo.certificates.add(newCertificate)
        }
        userRepository.save(professional)
    }

    @Transactional(rollbackFor = [SQLException::class, Exception::class])
    fun deleteCertificate(professionalId: Long, imgPath: String) {
        val professional = userService.getUserById(professionalId)
        val certificate = professional.professionalInfo.certificates.find { it.imgs.any { img -> img.equals(imgPath,ignoreCase = true) } } ?: throw BusinessException("No se ha encontrado el certificado o su path es incorrecto")
        certificate.imgs.removeIf{ it.equals(imgPath,ignoreCase = true) }
    }
}