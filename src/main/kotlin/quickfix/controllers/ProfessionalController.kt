package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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
    fun payDebt(@PathVariable professionalId : Long) =
        professionalService.payDebt(professionalId)

    @GetMapping("/professions/{professionalId}")
    @Operation(summary = "Obtener los servicios que puede brindar el profesional")
    fun getProfessions(@PathVariable professionalId : Long) : Set<Profession> =
        professionalService.getActiveProfessions(professionalId)


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
    fun addCertificate(@PathVariable professionalId : Long, @RequestBody certificateDto: NewCertificateDTO) =
        professionalService.addCertificate(professionalId, certificateDto)

    //TODO: Valen refactorizó esto y ya le agregó el model attribute. Corregir cuando se mergee
    @PostMapping("/certificates/{professionalId}/{certificateId}")
    @Operation(summary = "Agregar certificado")
    fun uploadCertificateImg(@PathVariable professionalId : Long, @PathVariable certificateId : Long, @RequestBody image: MultipartFile) =
        professionalService.uploadCertificateImg(professionalId, certificateId, image)

    //TODO: Valen refactorizó esto y ya le agregó el model attribute. Corregir cuando se mergee
    @DeleteMapping("/certificates/{professionalId}/{certificateId}")
    @Operation(summary = "Borrar un certificado")
    fun deleteCertificate(@PathVariable professionalId : Long, @PathVariable certificateId : Long) =
        professionalService.deleteCertificate(professionalId, certificateId)
}
