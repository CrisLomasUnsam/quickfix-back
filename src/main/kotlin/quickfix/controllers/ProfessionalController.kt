package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.professional.FinancesDTO
import quickfix.dto.professional.NewCertificateDTO
import quickfix.models.Certificate
import quickfix.models.Profession
import quickfix.services.ProfessionalService

@RestController
@RequestMapping("/professional")
@Tag(name = "Profesionales", description = "Operaciones realizadas desde un profesional")
class ProfessionalController (
    private val professionalService : ProfessionalService
    ) {

    @GetMapping("/finances/{professionalId}")
    @Operation(summary = "Finanzas del profesional (debt y balance)")
    fun getFinances(@PathVariable professionalId : Long) : FinancesDTO =
        professionalService.getBalanceAndDebt(professionalId)

    @GetMapping("/finances/earnings/{professionalId}")
    @Operation(summary = "Ganancias totales por fecha")
    fun getTotalEarnings(@PathVariable professionalId : Long, @RequestParam dateStr: String) : Double =
        professionalService.getTotalEarningsByDate(professionalId, dateStr)

    @PatchMapping("/finances/payDebt/{professionalId}")
    @Operation(summary = "Pagar deuda")
    fun payDebt(@PathVariable professionalId : Long, @RequestBody amount: Double) =
        professionalService.payDebt(professionalId, amount)

    @GetMapping("/professions/{professionalId}")
    @Operation(summary = "Obtener los servicios brindados")
    fun getProfessions(@PathVariable professionalId : Long) : List<Profession> =
        professionalService.getProfessions(professionalId)


    @PostMapping("/professions/{professionalId}")
    @Operation(summary = "Agregar servicio brindado")
    fun addProfession(@PathVariable professionalId : Long, @RequestBody profession: String) =
        professionalService.addProfession(professionalId, profession)

    @DeleteMapping("/professions/{professionalId}")
    @Operation(summary = "Eliminar servicio brindado")
    fun deleteProfession(@PathVariable professionalId : Long, @RequestBody profession: String) =
        professionalService.deleteProfession(professionalId, profession)

    @GetMapping("/certificates/{professionalId}")
    @Operation(summary = "Obtener certificados")
    fun getCertificates(@PathVariable professionalId: Long) : Set<Certificate> =
        professionalService.getCertificates(professionalId)

    @PostMapping("/certificates/{professionalId}")
    @Operation(summary = "Agregar certificado")
    fun addCertificate(@PathVariable professionalId : Long, @RequestBody dto: NewCertificateDTO) =
        professionalService.addCertificate(professionalId, dto)

    @DeleteMapping("/certificates/{professionalId}")
    @Operation(summary = "Borrar un certificado")
    fun deleteCertificate(@PathVariable professionalId : Long, @Parameter(description = "Imagen o nombre del cert") @RequestBody certificateNameOrImg: String) =
        professionalService.deleteCertificate(professionalId, certificateNameOrImg.trim())
}
