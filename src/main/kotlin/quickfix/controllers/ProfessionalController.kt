package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import quickfix.dto.professional.FinancesDTO
import quickfix.dto.professional.NewCertificateDTO
import quickfix.models.Certificate
import quickfix.models.Profession
import quickfix.services.ProfessionalService

@RestController
@RequestMapping("/professional")
class ProfessionalController(
    private val professionalService: ProfessionalService
) {

    @ModelAttribute("currentProfessionalId")
    fun getCurrentProfessionalId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @GetMapping("/finances")
    fun getFinances(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long): FinancesDTO =
        professionalService.getBalanceAndDebt(currentProfessionalId)

    @GetMapping("/finances/earnings")
    fun getTotalEarnings(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestParam dateStr: String
    ): Double =
        professionalService.getTotalEarningsByDate(currentProfessionalId, dateStr)

    @PatchMapping("/finances/payDebt")
    fun payDebt(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long) =
        professionalService.payDebt(currentProfessionalId)

    @GetMapping("/professions")
    fun getProfessions(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long): List<Profession> =
        professionalService.getProfessions(currentProfessionalId)

    @PostMapping("/professions")
    fun addProfession(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestBody profession: String
    ) =
        professionalService.addProfession(currentProfessionalId, profession)

    @DeleteMapping("/professions")
    fun deleteProfession(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestBody profession: String
    ) =
        professionalService.deleteProfession(currentProfessionalId, profession)

    @GetMapping("/certificates")
    fun getCertificates(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long): Set<Certificate> =
        professionalService.getCertificates(currentProfessionalId)

    @PostMapping("/certificates")
    fun addCertificate(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestBody dto: NewCertificateDTO
    ) =
        professionalService.addCertificate(currentProfessionalId, dto)

    @DeleteMapping("/certificates")
    fun deleteCertificate(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestBody certificateNameOrImg: String
    ) =
        professionalService.deleteCertificate(currentProfessionalId, certificateNameOrImg.trim())
}
