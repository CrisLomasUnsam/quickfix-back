package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.models.Job
import quickfix.services.JobService

@RestController
@RequestMapping("/jobs")
@CrossOrigin("*")
@Tag(name = "Jobs", description = "Operaciones relacionadas a los Jobs")

class JobController(
    val jobService: JobService
){
//    @GetMapping("/byUser/{id}")
//    @Operation(summary = "Obtener todos los jobs de un customer")
//    fun getJobsByUserId (@Parameter(description = "Id del user") @PathVariable id: Long) = jobService.getJobsByUser(id)

    @PostMapping
    fun createJob(@RequestBody job: Job) = jobService.createJob(job)

    @DeleteMapping
    fun deleteJob(@RequestBody job: Job) = jobService.deleteJob(job)

    @GetMapping("/{id}")
    fun getJobById(@PathVariable id: Long) = jobService.getJobById(id)

//    @PatchMapping("/{id}/complete")
//    fun setJobAsDone(@PathVariable id: Long) = jobService.setJobAsDone(id)
//
//    @PatchMapping("/{id}/cancel")
//    fun setJobAsCancelled(@PathVariable id: Long) = jobService.setJobAsCancelled(id)

//    @GetMapping("/filter/{userId}/{parameter}")
//    @Operation(summary = "Buscar jobs por filtro")
//    fun getJobsByParameters(
//        @Parameter(description = "Id del usuario")
//        @PathVariable userId: Long,
//        @Parameter(description = "Parametro de busqueda: string")
//        @PathVariable parameter: String) = jobService.getJobsByParameter(userId, parameter)
}

