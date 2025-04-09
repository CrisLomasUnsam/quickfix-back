package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.rating.RatingDTO
import quickfix.services.CustomerService
import quickfix.services.JobService
import quickfix.services.RatingService

@RestController
@RequestMapping("/customer")
@CrossOrigin("*")
@Tag(name = "Customer", description = "Operaciones relacionadas al customer")

class CustomerController(
    private val jobService: JobService,
    private val ratingService: RatingService,
    private val customerService: CustomerService
){

    @GetMapping("/jobs/{id}")
    @Operation(summary = "Obtener todos los jobs de un customer")
    fun getJobsByCustomerId(
        @Parameter(description = "Id del customer")
        @PathVariable id: Long) = jobService.getJobsByCustomer(id)

    @GetMapping("/filterJobs/{id}/{parameter}")
    @Operation(summary = "Buscar jobs por filtro")
    fun getJobsByParameters(
        @PathVariable id: Long,
        @Parameter(description = "Parametro de busqueda: string")
        @PathVariable parameter: String) = customerService.getJobsByParameter(id, parameter)

    @PostMapping("/rateProfessional")
    @Operation(summary = "Calificar un professional al concluir el job")
    fun rateProfessional(@RequestBody rating: RatingDTO) = ratingService.rateProfessional(rating)

}