package quickfix.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.services.JobService

@RestController
@RequestMapping("/jobs")
@CrossOrigin("*")
@Tag(name = "Jobs", description = "Operaciones relacionadas a los Jobs")

class JobController(
    val jobService: JobService
){
    @GetMapping("/{id}")
    fun getJobById(@PathVariable id: Int) = jobService.getJobById(id)
}