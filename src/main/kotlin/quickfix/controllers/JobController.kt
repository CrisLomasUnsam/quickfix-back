package quickfix.controllers

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
    @PostMapping
    fun createJob(@RequestBody job: Job) = jobService.createJob(job)

    @DeleteMapping
    fun deleteJob(@RequestBody job: Job) = jobService.deleteJob(job)

    @GetMapping("/{id}")
    fun getJobById(@PathVariable id: Int) = jobService.getJobById(id)

    @PatchMapping("/{id}/completed")
    fun setJobAsDone(@PathVariable id: Int) = jobService.setJobAsDone(id)

    @PatchMapping("/{id}/cancelled")
    fun setJobAsCancelled(@PathVariable id: Int) = jobService.setJobAsCancelled(id)
}

