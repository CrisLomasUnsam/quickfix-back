package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.JobDetailsDTO
import quickfix.dto.job.JobWithRatingDTO
import quickfix.dto.job.jobOffer.*
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.job.jobRequest.CustomerJobRequestDTO
import quickfix.dto.job.jobRequest.ProfessionalJobRequestDTO
import quickfix.dto.page.PageDTO
import quickfix.services.JobService

@RestController
@RequestMapping("/job")
@Tag(name = "Jobs", description = "Operaciones relacionadas a los Jobs")

class JobController(
    val jobService: JobService,
){

    @ModelAttribute("currentUserId")
    fun getCurrentCustomerId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @GetMapping("/jobDetails/{jobId}")
    @Operation(summary = "Obtiene los detalles de jobs aceptados por customer")
    fun getJobDetailsForCustomerByJobId(@ModelAttribute("currentUserId") currentUserId: Long, @PathVariable jobId: Long): JobDetailsDTO =
        jobService.getJobDetailsById(currentUserId, jobId)

    @GetMapping("/customer")
    @Operation(summary = "Obtiene todos los servicios pedidos por un usuario")
    fun findJobsByCustomerId(
        @ModelAttribute("currentUserId") currentCustomerId : Long, @RequestParam(required = false) pageNumber: Int? ) : PageDTO<JobWithRatingDTO> =
        PageDTO.toJobWithRatingPageDTO(jobService.findMyJobsByCustomerId(currentCustomerId, pageNumber))

    @GetMapping("/professional")
    @Operation(summary = "Obtiene todos los servicios realizados por un profesional")
    fun findJobsByProfessionalId(@ModelAttribute("currentUserId") currentProfessionalId : Long, @RequestParam (required = false) pageNumber: Int? ) : PageDTO<JobWithRatingDTO> =
        PageDTO.toJobWithRatingPageDTO(jobService.findMyJobsByProfessionalId(currentProfessionalId, pageNumber))

    @PatchMapping("/complete/{jobId}")
    fun setJobAsDone(@ModelAttribute("currentUserId") currentProfessionalId : Long, @PathVariable jobId: Long) =
        jobService.setJobAsDone(currentProfessionalId, jobId)

    @PatchMapping("/cancel/{jobId}")
    fun setJobAsCancelled(@ModelAttribute("currentUserId") currentUserId : Long, @PathVariable jobId: Long) =
        jobService.setJobAsCancelled(currentUserId, jobId)

    /*************************
     JOB OFFERS
     **************************/

    @GetMapping("/jobOffers")
    @Operation(summary = "Utilizado para el polling que devuelve las nuevas ofertas enviadas por los profesionales")
    fun getJobOffers(@ModelAttribute("currentUserId") currentCustomerId : Long, @RequestParam professionId: Long) : List<CustomerJobOfferDTO> =
        jobService.getJobOffers(currentCustomerId, professionId)

    @GetMapping("/myJobOffers")
    @Operation(summary = "Utilizado para el polling que devuelve las nuevas ofertas enviadas por los profesionales")
    fun getMyJobOffers(@ModelAttribute("currentUserId") currentProfessionalId : Long) : List<ProfessionalJobOfferDTO> =
        jobService.getMyJobOffers(currentProfessionalId)

    @PostMapping("/offerJob")
    @Operation(summary = "Utilizado para que el profesional pueda enviar su oferta a un determinado JobRequest")
    fun offerJob(@ModelAttribute("currentUserId") currentProfessionalId : Long, @RequestBody jobOffer : CreateJobOfferDTO) =
        jobService.offerJob(currentProfessionalId, jobOffer)

    @DeleteMapping("/jobOffer")
    @Operation(summary = "Utilizado para cancelar la oferta de un JobOffer")
    fun cancelJobOffer(@ModelAttribute("currentUserId") currentProfessionalId: Long, @RequestParam requestId: String) =
        jobService.cancelJobOffer(currentProfessionalId, requestId)

    @PostMapping("/acceptJobOffer")
    @Operation(summary = "Aceptar una oferta de trbajo")
    fun acceptJobOffer(@ModelAttribute("currentUserId") currentCustomerId: Long, @RequestBody acceptedJobOfferDTO : AcceptJobOfferDTO) =
        jobService.acceptJobOffer(currentCustomerId, acceptedJobOfferDTO)

    /*************************
    JOB REQUESTS
     **************************/

    @GetMapping("/myJobRequests")
    @Operation(summary = "Utilizado para que el customer pueda ver sus propias solicitudes activas")
    fun getMyJobRequests(@ModelAttribute("currentUserId") currentCustomerId : Long) : List<CustomerJobRequestDTO> =
        jobService.getMyJobRequests(currentCustomerId)

    @GetMapping("/jobRequests")
    @Operation(summary = "Utilizado para el polling que devuelve los servicios solicitados por los usuarios")
    fun getJobRequests(@ModelAttribute("currentUserId") currentProfessionalId : Long) : List<ProfessionalJobRequestDTO> =
        jobService.getJobRequests(currentProfessionalId)

    @GetMapping("/jobRequest")
    @Operation(summary = "Utilizado para ver una solicitud específica (sin ofertar) desde un profesional")
    fun getProfessionalJobRequest(@ModelAttribute("currentUserId") currentProfessionalId : Long, @RequestParam jobRequestId: String) : ProfessionalJobRequestDTO =
        ProfessionalJobRequestDTO.fromJobRequest(jobService.getNotOfferedJobRequest(currentProfessionalId, jobRequestId))

    @PostMapping("/requestJob")
    @Operation(summary = "Buscar profesionales disponibles para el job seleccionado por el customer")
    fun requestJob(@RequestBody jobRequest : JobRequestDTO) =
        jobService.requestJob(jobRequest)

    @DeleteMapping("/jobRequest")
    @Operation(summary = "Cancelar un job request")
    fun cancelJobRequest(@ModelAttribute("currentUserId") currentCustomerId : Long, @RequestParam professionId: Long) =
        jobService.cancelJobRequest(professionId, currentCustomerId)
}

