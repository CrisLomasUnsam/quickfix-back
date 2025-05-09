package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.JobDTO
import quickfix.dto.job.PageDTO
import quickfix.dto.job.jobOffer.AcceptedJobOfferDTO
import quickfix.dto.job.jobOffer.CancelJobOfferDTO
import quickfix.dto.job.jobOffer.CreateJobOfferDTO
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobRequest.CancelJobRequestDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.job.toDto
import quickfix.models.Job
import quickfix.services.JobService

@RestController
@RequestMapping("/job")
@Tag(name = "Jobs", description = "Operaciones relacionadas a los Jobs")

class JobController(
    val jobService: JobService
){

    @ModelAttribute("currentCustomerId")
    fun getCurrentCustomerId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @ModelAttribute("currentProfessionalId")
    fun getCurrentProfessionalId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @GetMapping("/customer")
    @Operation(summary = "Obtiene todos los servicios pedidos por un usuario")
    fun findJobsByCustomerId(
        @ModelAttribute("currentCustomerId") currentCustomerId : Long, @RequestParam pageNumber: Int) : PageDTO<JobDTO> =
        PageDTO.toDTO(jobService.findJobsByCustomerId(currentCustomerId, pageNumber).map{ job -> toDto(job)  })

    @GetMapping("/professional")
    @Operation(summary = "Obtiene todos los servicios realizados por un profesional")
    fun findJobsByProfessionalId(@ModelAttribute("currentProfessionalId") currentProfessionalId : Long, @RequestParam pageNumber: Int) : PageDTO<JobDTO> =
        PageDTO.toDTO(jobService.findJobsByProfessionalId(currentProfessionalId, pageNumber).map{ job -> toDto(job)  })

    @PatchMapping("/complete/{id}")
    fun setJobAsDone(@PathVariable id: Long) = jobService.setJobAsDone(id)

    @PatchMapping("/cancel/{id}")
    fun setJobAsCancelled(@PathVariable id: Long) = jobService.setJobAsCancelled(id)

    @GetMapping("/requestedJobs")
    @Operation(summary = "Buscar jobs solicitados como customer por filtro")
    fun getRequestedJobsByParameters(@ModelAttribute("currentCustomerId") currentCustomerId : Long, @RequestParam(required = false) parameter: String?): List<JobDTO> {
        val jobs = jobService.getJobsByParameter(currentCustomerId, parameter)
        return jobs.map {job -> toDto(job)  }
    }

    @GetMapping("/offeredJobs")
    @Operation(summary = "Buscar jobs realizados como profesional por filtro")
    fun getOfferedJobsByParameters(@ModelAttribute("currentProfessionalId") currentProfessionalId : Long, @RequestParam(required = false) parameter: String?): List<JobDTO> {
        val jobs = jobService.getJobsByParameter(currentProfessionalId, parameter)
        return jobs.map {job -> toDto(job)  }
    }

    /*************************
     JOB OFFERS
     **************************/

    @GetMapping("/jobOffers")
    @Operation(summary = "Utilizado para el polling que devuelve las nuevas ofertas enviadas por los profesionales")
    fun getJobOffers(@ModelAttribute("currentCustomerId") currentCustomerId : Long) : List<JobOfferDTO> =
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
    fun getJobRequests(@ModelAttribute("currentProfessionalId") currentProfessionalId : Long) : Set<JobRequestDTO> =
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

