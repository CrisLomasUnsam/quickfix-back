package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.UserRepository
import quickfix.dto.professional.FinancesDTO
import quickfix.dto.professional.NewCertificateDTO
import quickfix.models.Certificate
import quickfix.models.Profession
import quickfix.models.ProfessionalInfo
import quickfix.utils.comission
import quickfix.utils.datifyStringMonthAndYear
import quickfix.utils.exceptions.BusinessException

@Service
class ProfessionalService(
    val userService: UserService,
    val professionService: ProfessionService,
    private val userRepository: UserRepository,
)  {

    fun getProfessionIds(professionalId : Long) : Set<Long> {
        val professions = userService.getActiveProfessionsByUserId(professionalId)
        return professions.map { it.id }.toSet()
    }

    fun getBalanceAndDebt(professionalId: Long): FinancesDTO {
        val professional = userService.getUserById(professionalId)
        val financesDTO = FinancesDTO(
            balance = professional.professionalInfo.balance,
            debt = professional.professionalInfo.debt
        )
        return financesDTO
    }

    fun getProfessions(proffesionalId: Long) : List<Profession> {
        userService.assertUserExists(proffesionalId)
        val professionIds : Set<Long> = this.getProfessionIds(proffesionalId)
        return professionIds.map { professionService.getProfessionById(it) }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun addProfession(professionalId: Long, professionName: String) {
        val professional = userService.getUserById(professionalId)
        val profession = professionService.getByNameIgnoreCase(professionName)

        if (professional.professionalInfo.hasActiveProfession(profession.id))
            throw BusinessException("La profesión ya forma parte de sus servicios")

        professional.professionalInfo.addProfession(profession)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deleteProfession(professionalId: Long, professionName: String) {
        val professional = userService.getUserById(professionalId)
        val professionId = professionService.getByNameIgnoreCase(professionName).id

        if (!professional.professionalInfo.hasActiveProfession(professionId))
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
        professionalInfo.validateCertificateAlreadyExists(newCert.name, profession)

        val newCertificate = Certificate().apply {
            this.name = newCert.name
            this.profession = profession
            this.img = newCert.img.trim()
        }
        professionalInfo.addCertificate(newCertificate)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deleteCertificate(professionalId: Long, certificateNameOrImg: String) {
        val professionalInfo = userService.getProfessionalInfo(professionalId)
        professionalInfo.deleteCertificate(certificateNameOrImg)
    }

    fun getTotalEarningsByDate(professionalId: Long, dateStr: String): Double {
        val dateStart = datifyStringMonthAndYear(dateStr)
        val dateEnd = dateStart.withDayOfMonth(dateStart.lengthOfMonth())
        val netEarnings = userRepository.getEarningsByProfessionalIdAndDateRange(professionalId, dateStart, dateEnd) ?: 0.0
        return netEarnings - comission(netEarnings)
    }

    @Transactional
    fun payDebt(professionalId: Long, amount: Double) {
        val professional = userService.getUserById(professionalId).professionalInfo
        professional.payDebt(amount)
    }
}