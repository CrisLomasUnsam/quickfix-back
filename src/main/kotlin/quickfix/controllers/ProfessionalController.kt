package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import quickfix.dto.professional.FinancesDTO
import quickfix.dto.professional.NewCertificateDTO
import quickfix.dto.professional.ProfessionDTO
import quickfix.models.Certificate
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
    fun getProfessionalProfessions(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long) : List<ProfessionDTO> =
        professionalService.getProfessionalProfessions(currentProfessionalId).map { ProfessionDTO.toDto(it) }.sortedBy { it.name }

    @PostMapping("/profession")
    @Operation(summary = "Agregar servicio brindado")
    fun addProfession(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestParam professionId: Long
    ) =
        professionalService.addProfession(currentProfessionalId, professionId)

    @DeleteMapping("/profession")
    @Operation(summary = "Eliminar servicio brindado")
    fun deleteProfession(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestParam professionId: Long
    ) =
        professionalService.deleteProfession(currentProfessionalId, professionId)

    @PatchMapping("/profession/activate")
    @Operation(summary = "Activar servicio brindado")
    fun activateProfession(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long, @RequestParam professionId: Long) =
        professionalService.switchProfessionStatus(currentProfessionalId, professionId, activate = true)

    @PatchMapping("/profession/deactivate")
    @Operation(summary = "Desactivar servicio brindado")
    fun deactivateProfession(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long, @RequestParam professionId: Long) =
        professionalService.switchProfessionStatus(currentProfessionalId, professionId, activate = false)

    @GetMapping("/certificates")
    @Operation(summary = "Obtener certificados")
    fun getCertificates(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long): Set<Certificate> =
        professionalService.getCertificates(currentProfessionalId)

    @PostMapping("/certificate")
    @Operation(summary = "Agregar certificado")
    fun addCertificate(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long, @RequestBody certificateDto: NewCertificateDTO) =
        professionalService.addCertificate(currentProfessionalId, certificateDto)

    @PostMapping("/certificate/{certificateId}")
    @Operation(summary = "Agregar certificado")
    fun uploadCertificateImg(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long, @PathVariable certificateId : Long, @RequestBody image: MultipartFile) =
        professionalService.uploadCertificateImg(currentProfessionalId, certificateId, image)

    @DeleteMapping("/certificate/{certificateId}")
    @Operation(summary = "Borrar un certificado")
    fun deleteCertificate(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long, @PathVariable certificateId : Long) =
        professionalService.deleteCertificate(currentProfessionalId, certificateId)
}
