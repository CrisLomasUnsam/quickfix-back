package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.CancelJobOfferDTO
import quickfix.dto.job.JobOfferDTO
import quickfix.dto.job.JobRequestDTO
import quickfix.services.ProfessionalService

@RestController
@RequestMapping("/professional")
@CrossOrigin (origins = ["*"])
@Tag(name = "Profesionales", description = "Operaciones realizadas desde un profesional")
class ProfessionalController (
    val professionalService : ProfessionalService
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
    fun offerJob(@RequestBody jobOffer : JobOfferDTO) =
        professionalService.offerJob(jobOffer)

    @DeleteMapping("/jobOffers")
    @Operation(summary = "Utilizado para cancelar la oferta de un JobOffer")
    fun cancelJobOffer(@RequestBody cancelJobOffer: CancelJobOfferDTO) =
        professionalService.cancelJobOffer(cancelJobOffer)

}