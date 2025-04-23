package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.jobOffer.AcceptedJobOfferDTO
import quickfix.dto.job.JobDTO
import quickfix.dto.job.toDto
import quickfix.models.Job
import quickfix.services.JobService

@RestController
@RequestMapping("/job")
@CrossOrigin("*")
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

    @PostMapping("/acceptJobOffer")
    @Operation(summary = "Aceptar una oferta de trbajo")
    fun acceptJobOffer(@RequestBody acceptedJobOfferDTO : AcceptedJobOfferDTO)
            = jobService.acceptJobOffer(acceptedJobOfferDTO)

    @PatchMapping("/{id}/complete")
    fun setJobAsDone(@PathVariable id: Long) = jobService.setJobAsDone(id)
//
//    @PatchMapping("/{id}/cancel")
//    fun setJobAsCancelled(@PathVariable id: Long) = jobService.setJobAsCancelled(id)

    @GetMapping("/filter/{userId}")
    @Operation(summary = "Buscar jobs por filtro")
    fun getJobsByParameters(
        @Parameter(description = "Id del usuario")
        @PathVariable userId: Long,
        @Parameter(description = "Parametro de busqueda: string")
        @RequestParam(required = false) parameter: String?
    ): List<JobDTO> {
        val jobs = jobService.getJobsByParameter(userId, parameter)
        return jobs.map {job -> toDto(job)  }
    }
}

