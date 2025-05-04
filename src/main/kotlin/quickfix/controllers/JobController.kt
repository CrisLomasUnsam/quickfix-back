package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.JobDTO
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

    @GetMapping("/customer/{id}")
    @Operation(summary = "Obtiene todos los servicios pedidos por un usuario")
    fun findJobsByCustomerId(@PathVariable id: Long) : List<Job> =
        jobService.findJobsByCustomerId(id)

    @GetMapping("/professional/{id}")
    @Operation(summary = "Obtiene todos los servicios realizados por un profesional")
    fun findJobsByProfessionalId(@PathVariable id: Long) : List<Job> =
        jobService.findJobsByProfessionalId(id)

    @PatchMapping("/{id}/complete")
    fun setJobAsDone(@PathVariable id: Long) = jobService.setJobAsDone(id)

    @PatchMapping("/{id}/cancel")
    fun setJobAsCancelled(@PathVariable id: Long) = jobService.setJobAsCancelled(id)

    @GetMapping("/filter/{userId}")
    @Operation(summary = "Buscar jobs por filtro")
    fun getJobsByParameters(@PathVariable userId: Long, @RequestParam(required = false) parameter: String?): List<JobDTO> {
        val jobs = jobService.getJobsByParameter(userId, parameter)
        return jobs.map {job -> toDto(job)  }
    }

    /*************************
     JOB OFFERS
     **************************/

    @GetMapping("/jobOffers/{customerId}")
    @Operation(summary = "Utilizado para el polling que devuelve las nuevas ofertas enviadas por los profesionales")
    fun getJobOffers(@PathVariable customerId: Long) : List<JobOfferDTO> =
        jobService.getJobOffers(customerId)

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

    @GetMapping("/jobRequests/{professionalId}")
    @Operation(summary = "Utilizado para el polling que devuelve los servicios solicitados por los usuarios")
    fun getJobRequests(@PathVariable professionalId : Long) : Set<JobRequestDTO> =
        jobService.getJobRequests(professionalId)

    @PostMapping("/requestJob")
    @Operation(summary = "Buscar profesionales disponibles para el job seleccionado por el customer")
    fun requestJob(@RequestBody jobRequest : JobRequestDTO) =
        jobService.requestJob(jobRequest)

    @DeleteMapping("/requestJob")
    @Operation(summary = "Cancelar un job request")
    fun cancelJobRequest(@RequestBody cancelJobRequest : CancelJobRequestDTO) =
        jobService.cancelJobRequest(cancelJobRequest)
}

