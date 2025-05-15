package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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

    @GetMapping("/professions/{professionalId}")
    @Operation(summary = "Obtener los servicios que puede brindar el profesional")
    fun getProfessions(@PathVariable professionalId: Long): List<Profession> =
        professionalService.getProfessions(professionalId)

    @PostMapping("/professions/{professionalId}")
    @Operation(summary = "Agregar servicio brindado")
    fun addProfession(@PathVariable professionalId: Long, @RequestBody profession: String) =
        professionalService.addProfession(professionalId, profession)

    @DeleteMapping("/professions/{professionalId}")
    @Operation(summary = "Eliminar servicio brindado")
    fun deleteProfession(@PathVariable professionalId: Long, @RequestBody profession: String) =
        professionalService.deleteProfession(professionalId, profession)

    @GetMapping("/certificates/{professionalId}")
    @Operation(summary = "Obtener certificados")
    fun getCertificates(@PathVariable professionalId: Long): Set<Certificate> =
        professionalService.getCertificates(professionalId)

    @PostMapping("/certificates/{professionalId}")
    @Operation(summary = "Agregar certificado")
    fun addCertificate(@PathVariable professionalId: Long, @RequestBody dto: NewCertificateDTO) =
        professionalService.addCertificate(professionalId, dto)

    @DeleteMapping("/certificates/{professionalId}")
    @Operation(summary = "Borrar un certificado")
    fun deleteCertificate(
        @PathVariable professionalId: Long,
        @Parameter(description = "Imagen o nombre del cert") @RequestBody certificateNameOrImg: String
    ) =
        professionalService.deleteCertificate(professionalId, certificateNameOrImg.trim())
}
