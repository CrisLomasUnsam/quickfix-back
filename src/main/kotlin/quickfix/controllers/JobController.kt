package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.JobDTO
import quickfix.dto.job.PageDTO
import quickfix.dto.job.jobOffer.AcceptJobOfferDTO
import quickfix.dto.job.jobOffer.CreateJobOfferDTO
import quickfix.dto.job.jobOffer.CustomerJobOfferDTO
import quickfix.dto.job.jobRequest.ProfessionalJobRequestDTO
import quickfix.dto.job.jobRequest.CustomerJobRequestDTO
import quickfix.dto.job.toDto
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
        @ModelAttribute("currentUserId") currentCustomerId : Long, @RequestParam pageNumber: Int) : PageDTO<JobDTO> =
        PageDTO.toDTO(jobService.findJobsByCustomerId(currentCustomerId, pageNumber).map{ job -> toDto(job)  })

    @GetMapping("/professional")
    @Operation(summary = "Obtiene todos los servicios realizados por un profesional")
    fun findJobsByProfessionalId(@ModelAttribute("currentUserId") currentProfessionalId : Long, @RequestParam pageNumber: Int) : PageDTO<JobDTO> =
        PageDTO.toDTO(jobService.findJobsByProfessionalId(currentProfessionalId, pageNumber).map{ job -> toDto(job)  })

    @PatchMapping("/complete/{jobId}")
    fun setJobAsDone(@ModelAttribute("currentUserId") currentProfessionalId : Long, @PathVariable jobId: Long) =
        jobService.setJobAsDone(currentProfessionalId, jobId)

    @PatchMapping("/cancel/{jobId}")
    fun setJobAsCancelled(@ModelAttribute("currentUserId") currentUserId : Long, @PathVariable jobId: Long) =
        jobService.setJobAsCancelled(currentUserId, jobId)

    @GetMapping("/requestedJobs")
    @Operation(summary = "Buscar jobs solicitados como customer por filtro")
    fun getRequestedJobsByParameters(@ModelAttribute("currentUserId") currentCustomerId : Long, @RequestParam(required = false) parameter: String?): List<JobDTO> {
        val jobs = jobService.getJobsByParameter(currentCustomerId, parameter)
        return jobs.map {job -> toDto(job)  }
    }

    @GetMapping("/offeredJobs")
    @Operation(summary = "Buscar jobs realizados como profesional por filtro")
    fun getOfferedJobsByParameters(@ModelAttribute("currentUserId") currentProfessionalId : Long, @RequestParam(required = false) parameter: String?): List<JobDTO> {
        val jobs = jobService.getJobsByParameter(currentProfessionalId, parameter)
        return jobs.map {job -> toDto(job)  }
    }

    /*************************
     JOB OFFERS
     **************************/

    @GetMapping("/jobOffers")
    @Operation(summary = "Utilizado para el polling que devuelve las nuevas ofertas enviadas por los profesionales")
    fun getJobOffers(@ModelAttribute("currentUserId") currentCustomerId : Long) : List<CustomerJobOfferDTO> =
        jobService.getJobOffers(currentCustomerId)

    @GetMapping("/myJobOffers")
    @Operation(summary = "Utilizado para el polling que devuelve las nuevas ofertas enviadas por los profesionales")
    fun getMyJobOffers(@ModelAttribute("currentUserId") currentProfessionalId : Long) : List<CustomerJobOfferDTO> =
        jobService.getMyJobOffers(currentProfessionalId)

    @PostMapping("/jobOffers")
    @Operation(summary = "Utilizado para que el profesional pueda enviar su oferta a un determinado JobRequest")
    fun offerJob(@RequestBody jobOffer : CreateJobOfferDTO) =
        jobService.offerJob(jobOffer)

    @DeleteMapping("/jobOffers")
    @Operation(summary = "Utilizado para cancelar la oferta de un JobOffer")
    fun cancelJobOffer(@ModelAttribute("currentUserId") currentProfessionalId: Long, @RequestParam requestId: String) =
        jobService.cancelJobOffer(currentProfessionalId, requestId)

    @PostMapping("/acceptJobOffer")
    @Operation(summary = "Aceptar una oferta de trbajo")
    fun acceptJobOffer(@RequestBody acceptedJobOfferDTO : AcceptJobOfferDTO) =
        jobService.acceptJobOffer(acceptedJobOfferDTO)

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

    @PostMapping("/requestJob")
    @Operation(summary = "Buscar profesionales disponibles para el job seleccionado por el customer")
    fun requestJob(@RequestBody jobRequest : ProfessionalJobRequestDTO) =
        jobService.requestJob(jobRequest)

    @DeleteMapping("/requestJob")
    @Operation(summary = "Cancelar un job request")
    fun cancelJobRequest(@ModelAttribute("currentUserId") currentCustomerId : Long, @RequestParam professionId: Long) =
        jobService.cancelJobRequest(currentCustomerId, professionId)
}

