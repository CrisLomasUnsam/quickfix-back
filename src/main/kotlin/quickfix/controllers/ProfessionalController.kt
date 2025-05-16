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
@Tag(name = "Profesionales", description = "Operaciones realizadas desde un profesional")
class ProfessionalController(
    private val professionalService: ProfessionalService
) {

    @ModelAttribute("currentProfessionalId")
    fun getCurrentProfessionalId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @GetMapping("/finances")
    @Operation(summary = "Finanzas del profesional (debt y balance)")
    fun getFinances(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long): FinancesDTO =
        professionalService.getBalanceAndDebt(currentProfessionalId)

    @GetMapping("/finances/earnings")
    @Operation(summary = "Ganancias totales por fecha")
    fun getTotalEarnings(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestParam dateStr: String
    ): Double =
        professionalService.getTotalEarningsByDate(currentProfessionalId, dateStr)

    @PatchMapping("/finances/payDebt")
    @Operation(summary = "Pagar deuda")
    fun payDebt(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long) =
        professionalService.payDebt(currentProfessionalId)

    @GetMapping("/professions")
    @Operation(summary = "Obtener los servicios que puede brindar el profesional")
    fun getProfessions(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long): List<Profession> =
        professionalService.getProfessions(currentProfessionalId)

    @PostMapping("/professions")
    @Operation(summary = "Agregar servicio brindado")
    fun addProfession(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestBody profession: String
    ) =
        professionalService.addProfession(currentProfessionalId, profession)

    @DeleteMapping("/professions")
    @Operation(summary = "Eliminar servicio brindado")
    fun deleteProfession(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestBody profession: String
    ) =
        professionalService.deleteProfession(currentProfessionalId, profession)

    @GetMapping("/certificates")
    @Operation(summary = "Obtener certificados")
    fun getCertificates(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long): Set<Certificate> =
        professionalService.getCertificates(currentProfessionalId)

    @PostMapping("/certificates")
    @Operation(summary = "Agregar certificado")
    fun addCertificate(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestBody dto: NewCertificateDTO
    ) =
        professionalService.addCertificate(currentProfessionalId, dto)

    @DeleteMapping("/certificates")
    @Operation(summary = "Borrar un certificado")
    fun deleteCertificate(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestBody certificateNameOrImg: String
    ) =
        professionalService.deleteCertificate(currentProfessionalId, certificateNameOrImg.trim())
}
