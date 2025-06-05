package quickfix.services

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import quickfix.dao.UserRepository
import quickfix.dto.professional.FinancesDTO
import quickfix.dto.professional.NewCertificateDTO
import quickfix.models.Certificate
import quickfix.models.ProfessionalInfo
import quickfix.models.ProfessionalProfession
import quickfix.utils.commission
import quickfix.utils.events.OnDebtPaidEvent
import quickfix.utils.exceptions.ProfessionalException
import quickfix.utils.functions.datifyStringMonthAndYear

@Service
class ProfessionalService(
    val userService: UserService,
    val professionService: ProfessionService,
    private val userRepository: UserRepository,
    private val imageService: ImageService,
    val eventPublisher: ApplicationEventPublisher
)  {

    fun getActiveProfessionIds(professionalId : Long) : Set<Long> {
        val professions = userService.getActiveProfessionsByUserId(professionalId)
        return professions.map { it.id }.toSet()
    }

    fun getBalanceAndDebt(professionalId: Long): FinancesDTO {
        val professional = userService.getById(professionalId)
        val financesDTO = FinancesDTO(
            balance = professional.professionalInfo.balance,
            debt = professional.professionalInfo.debt
        )
        return financesDTO
    }

    fun getProfessionalProfessions(professionalId: Long) : Set<ProfessionalProfession> {
        val professional = userService.getById(professionalId)
        return professional.professionalInfo.professionalProfessions
    }
    fun getProfessionalSubscription(professionalId: Long): ProfessionalInfo {
        val professional = userService.getById(professionalId)
        return professional.professionalInfo
    }

    @Transactional(rollbackFor = [Exception::class])
    fun addProfession(professionalId: Long, professionId: Long) {
        val professional = userService.getProfessionalInfo(professionalId)
        val profession = professionService.getProfessionById(professionId)

        if (professional.hasProfession(profession.id))
            throw ProfessionalException("La profesión ya forma parte de sus servicios")

        professional.addProfession(profession)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deleteProfession(professionalId: Long, professionId: Long) {
        val professional = userService.getProfessionalInfo(professionalId)
        if (!professional.hasProfession(professionId))
            throw ProfessionalException("La profesión no forma parte de sus servicios")
        professional.removeProfession(professionId)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun switchProfessionStatus(professionalId: Long, professionId: Long, activate : Boolean) {
        val professional = userService.getProfessionalInfo(professionalId)
        if (!professional.hasProfession(professionId))
            throw ProfessionalException("La profesión no forma parte de sus servicios")
        professional.setProfessionStatus(professionId, activate)
    }

    @Transactional(readOnly = true)
    fun getCertificates(professionalId: Long): Set<Certificate> {
       val professional = userService.getById(professionalId)
       return professional.professionalInfo.certificates.toSet()
    }

    @Transactional(rollbackFor = [Exception::class])
    fun addCertificate(professionalId: Long, newCert: NewCertificateDTO) {
        val professionalInfo : ProfessionalInfo = userService.getProfessionalInfo(professionalId)
        val profession = professionService.getProfessionById(newCert.professionId)
        professionalInfo.validateCertificateAlreadyExists(newCert.name, profession)

        val newCertificate = Certificate().apply {
            this.name = newCert.name
            this.profession = profession
        }
        professionalInfo.addCertificate(newCertificate)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun uploadCertificateImg(professionalId: Long, certificateId: Long, certificate: MultipartFile) {
        val professional = userService.getProfessionalInfo(professionalId)
        professional.assertCertificateExists(certificateId)
        professional.setCertificateHasImage(certificateId)
        imageService.uploadCertificate(certificateId, certificate)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deleteCertificate(professionalId: Long, certificateId: Long) {
        val professionalInfo = userService.getProfessionalInfo(professionalId)
        professionalInfo.deleteCertificate(certificateId)
        imageService.deleteCertificate(certificateId)
    }

    fun getTotalEarningsByDate(professionalId: Long, dateStr: String): Double {
        val dateStart = datifyStringMonthAndYear(dateStr)
        val dateEnd = dateStart.withDayOfMonth(dateStart.lengthOfMonth())
        val netEarnings = userRepository.getEarningsByProfessionalIdAndDateRange(professionalId, dateStart, dateEnd) ?: 0.0
        return netEarnings - commission(netEarnings)
    }

    @Transactional
    fun payDebt(professionalId: Long) {
        val professionalInfo = userService.getById(professionalId)
        val professional = professionalInfo.professionalInfo
        professional.payDebt()
        eventPublisher.publishEvent(OnDebtPaidEvent(professionalInfo.name, professionalInfo.mail))
    }
}