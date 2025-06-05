package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import quickfix.dto.mercadopago.SubscriptionStatusDTO
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

    /**
     * Obtiene la suscripción del profesional. Las posibles combinaciones de respuesta son:
     *
     * 1. El profesional **nunca tuvo suscripción** y **no está en período de prueba**:
     *    ```json
     *    {
     *      "subscriptionId": null,
     *      "status": "NONE",
     *      "nextPaymentDate": null
     *    }
     *    ```
     *
     * 2. El profesional **no tiene suscripción activa** pero **está en período de prueba**:
     *    ```json
     *    {
     *      "subscriptionId": null,
     *      "status": "AUTHORIZED",
     *      "nextPaymentDate": "20xx-12-01T00:00:00Z" // fecha de finalización del trial (30 días desde su activación)
     *    }
     *    ```
     *
     * 3. El profesional **tuvo una suscripción activa**, pero **ya venció o la cancelo(no confundir con cancelled)** y **ya tuvo periodo de prueba**:
     *    ```json
     *    {
     *      "subscriptionId": "1234567890bacdef",
     *      "status": "PAUSED",
     *      "nextPaymentDate": "20xx-12-01T00:00:00Z" // fecha que estaria en el pasado.
     *    }
     *    ```
     * 4. El profesional **tiene una suscripción activa**:
     *    ```json
     *    {
     *      "id": "1234567890bacdef",
     *      "status": "AUTHORIZED"
     *      "nextPaymentDate": "20xx-12-01T00:00:00Z" // fecha valida.
     *    }
     *    ```
     * 5. El profesional **tiene una suscripcion activa** pero **pendiente** de pago:
     *    ```json
     *    {
     *      "id": "1234567890bacdef",
     *      "status": "PENDING",
     *      "nextPaymentDate": "20xx-12-01T00:00:00Z" // fecha valida.
     *    }
     *    ```
     * 6. El profesional **tuvo una suscripcion activa** pero **por motivos externos** se canceló la misma, el id NO ES VALIDO DEBE BORRARSE dado que no se puede reactivar, :
     *    ```json
     *    {
     *      "id": "1234567890bacdef",
     *      "status": "CANCELLED",
     *      "nextPaymentDate": "20xx-12-01T00:00:00Z" // fecha valida.
     *    }
     *    ```
     */

    @GetMapping("/subscription")
    @Operation(summary = "Obtener la suscripción del profesional")
    fun getProfessionalSubscription(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long): SubscriptionStatusDTO =
        professionalService.getProfessionalSubscription(currentProfessionalId)
            .let { SubscriptionStatusDTO.toDTO(it) }


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
    fun getProfessionalProfessions(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long): List<ProfessionDTO> =
        professionalService.getProfessionalProfessions(currentProfessionalId).map { ProfessionDTO.toDto(it) }
            .sortedBy { it.name }

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
    fun activateProfession(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestParam professionId: Long
    ) =
        professionalService.switchProfessionStatus(currentProfessionalId, professionId, activate = true)

    @PatchMapping("/profession/deactivate")
    @Operation(summary = "Desactivar servicio brindado")
    fun deactivateProfession(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestParam professionId: Long
    ) =
        professionalService.switchProfessionStatus(currentProfessionalId, professionId, activate = false)

    @GetMapping("/certificates")
    @Operation(summary = "Obtener certificados")
    fun getCertificates(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long): Set<Certificate> =
        professionalService.getCertificates(currentProfessionalId)

    @PostMapping("/certificate")
    @Operation(summary = "Agregar certificado")
    fun addCertificate(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestBody certificateDto: NewCertificateDTO
    ) =
        professionalService.addCertificate(currentProfessionalId, certificateDto)

    @PostMapping("/certificate/{certificateId}")
    @Operation(summary = "Agregar certificado")
    fun uploadCertificateImg(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @PathVariable certificateId: Long,
        @RequestBody image: MultipartFile
    ) =
        professionalService.uploadCertificateImg(currentProfessionalId, certificateId, image)

    @DeleteMapping("/certificate/{certificateId}")
    @Operation(summary = "Borrar un certificado")
    fun deleteCertificate(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @PathVariable certificateId: Long
    ) =
        professionalService.deleteCertificate(currentProfessionalId, certificateId)
}
