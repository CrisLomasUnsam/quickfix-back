package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.jobOffer.CancelJobOfferDTO
import quickfix.dto.job.jobOffer.CreateJobOfferDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.professional.FinancesDTO
import quickfix.dto.professional.NewCertificateDTO
import quickfix.models.Certificate
import quickfix.services.ProfessionalService

@RestController
@RequestMapping("/professional")
@CrossOrigin (origins = ["*"])
@Tag(name = "Profesionales", description = "Operaciones realizadas desde un profesional")
class ProfessionalController (
    private val professionalService : ProfessionalService
    ) {

    /*************************
        JOB REQUEST METHODS
     **************************/

    @GetMapping("/jobRequests/{professionalId}")
    @Operation(summary = "Utilizado para el polling que devuelve los servicios solicitados por los usuarios")
    fun getJobRequests(@PathVariable professionalId : Long) : Set<JobRequestDTO> =
        professionalService.getJobRequests(professionalId)

    /*************************
        JOB OFFER METHODS
     **************************/

    @PostMapping("/jobOffers")
    @Operation(summary = "Utilizado para que el profesional pueda enviar su oferta a un determinado JobRequest")
    fun offerJob(@RequestBody jobOffer : CreateJobOfferDTO) =
        professionalService.offerJob(jobOffer)

    @DeleteMapping("/jobOffers")
    @Operation(summary = "Utilizado para cancelar la oferta de un JobOffer")
    fun cancelJobOffer(@RequestBody cancelJobOffer: CancelJobOfferDTO) =
        professionalService.cancelJobOffer(cancelJobOffer)

    /*************************
        OTHER METHODS
     **************************/

    @GetMapping("/{professionalId}")
    @Operation(summary = "Finanzas del profesional")
    fun getFinances(@PathVariable professionalId : Long) : FinancesDTO =
        professionalService.getFinances(professionalId)

    @PostMapping("/services/{professionalId}")
    @Operation(summary = "Agregar servicio brindado")
    fun addProfession(@PathVariable professionalId : Long, @RequestBody profession: String) =
        professionalService.addProfession(professionalId, profession)

    @DeleteMapping("/services/{professionalId}")
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
    fun deleteCertificate(@PathVariable professionalId : Long, @Parameter(description = "Path de la imagen") @RequestBody imgPath: String) =
        professionalService.deleteCertificate(professionalId, imgPath.trim())
}
