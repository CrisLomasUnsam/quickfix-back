package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.JobBasicInfoDTO
import quickfix.dto.job.JobWithRatingDTO
import quickfix.dto.job.PageDTO
import quickfix.dto.job.jobOffer.AcceptedJobOfferDTO
import quickfix.dto.job.jobOffer.CancelJobOfferDTO
import quickfix.dto.job.jobOffer.CreateJobOfferDTO
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobRequest.CancelJobRequestDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.services.JobService

@RestController
@RequestMapping("/job")
@Tag(name = "Jobs", description = "Operaciones relacionadas a los Jobs")

class JobController(
    val jobService: JobService
){

    @ModelAttribute("currentUserId")
    fun getCurrentCustomerId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @GetMapping("/customer")
    @Operation(summary = "Obtiene todos los servicios pedidos por un usuario")
    fun findJobsByCustomerId(
        @ModelAttribute("currentUserId") currentCustomerId : Long, @RequestParam pageNumber: Int) : PageDTO<JobWithRatingDTO> =
        PageDTO.toDTO(jobService.findJobsByCustomerId(currentCustomerId, pageNumber))

    @GetMapping("/professional")
    @Operation(summary = "Obtiene todos los servicios realizados por un profesional")
    fun findJobsByProfessionalId(@ModelAttribute("currentUserId") currentProfessionalId : Long, @RequestParam pageNumber: Int) : PageDTO<JobWithRatingDTO> =
        PageDTO.toDTO(jobService.findJobsByProfessionalId(currentProfessionalId, pageNumber))

    @PatchMapping("/complete/{jobId}")
    fun setJobAsDone(@ModelAttribute("currentUserId") currentProfessionalId : Long, @PathVariable jobId: Long) =
        jobService.setJobAsDone(currentProfessionalId, jobId)

    @PatchMapping("/cancel/{jobId}")
    fun setJobAsCancelled(@ModelAttribute("currentUserId") currentUserId : Long, @PathVariable jobId: Long) =
        jobService.setJobAsCancelled(currentUserId, jobId)

    @GetMapping("/requestedJobs")
    @Operation(summary = "Buscar jobs solicitados como customer por filtro")
    fun getRequestedJobsByParameters(@ModelAttribute("currentUserId") currentCustomerId : Long, @RequestParam(required = false) parameter: String?): List<JobBasicInfoDTO> =
        jobService.getJobsByParameter(currentCustomerId, parameter)


    @GetMapping("/offeredJobs")
    @Operation(summary = "Buscar jobs realizados como profesional por filtro")
    fun getOfferedJobsByParameters(@ModelAttribute("currentUserId") currentProfessionalId : Long, @RequestParam(required = false) parameter: String?): List<JobBasicInfoDTO> =
        jobService.getJobsByParameter(currentProfessionalId, parameter)


    /*************************
     JOB OFFERS
     **************************/

    @GetMapping("/jobOffers")
    @Operation(summary = "Utilizado para el polling que devuelve las nuevas ofertas enviadas por los profesionales")
    fun getJobOffers(@ModelAttribute("currentUserId") currentCustomerId : Long) : List<JobOfferDTO> =
        jobService.getJobOffers(currentCustomerId)

    @PostMapping("/jobOffers")
    @Operation(summary = "Utilizado para que el profesional pueda enviar su oferta a un determinado JobRequest")
    fun offerJob(@RequestBody jobOffer : CreateJobOfferDTO) =
        jobService.offerJob(jobOffer)

    @DeleteMapping("/jobOffers")
    @Operation(summary = "Utilizado para cancelar la oferta de un JobOffer")
    fun cancelJobOffer(@RequestBody cancelJobOffer: CancelJobOfferDTO) =
        jobService.cancelJobOffer(cancelJobOffer)

    @PostMapping("/acceptJobOffer")
    @Operation(summary = "Aceptar una oferta de trbajo")
    fun acceptJobOffer(@RequestBody acceptedJobOfferDTO : AcceptedJobOfferDTO) =
        jobService.acceptJobOffer(acceptedJobOfferDTO)

    /*************************
    JOB REQUESTS
     **************************/

    @GetMapping("/jobRequests")
    @Operation(summary = "Utilizado para el polling que devuelve los servicios solicitados por los usuarios")
    fun getJobRequests(@ModelAttribute("currentUserId") currentProfessionalId : Long) : Set<JobRequestDTO> =
        jobService.getJobRequests(currentProfessionalId)

    @PostMapping("/requestJob")
    @Operation(summary = "Buscar profesionales disponibles para el job seleccionado por el customer")
    fun requestJob(@RequestBody jobRequest : JobRequestDTO) =
        jobService.requestJob(jobRequest)

    @DeleteMapping("/requestJob")
    @Operation(summary = "Cancelar un job request")
    fun cancelJobRequest(@RequestBody cancelJobRequest : CancelJobRequestDTO) =
        jobService.cancelJobRequest(cancelJobRequest)
}

