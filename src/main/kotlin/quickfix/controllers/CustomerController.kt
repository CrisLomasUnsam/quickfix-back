package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.rating.RatingDTO
import quickfix.models.Rating
import quickfix.services.CustomerService
import quickfix.services.JobService
import quickfix.services.RatingService
import quickfix.utils.JobSearchParameters

@RestController
@RequestMapping("/customer")
@CrossOrigin("*")
@Tag(name = "Customer", description = "Operaciones relacionadas al customer")

class CustomerController(
    private val jobService: JobService,
    private val ratingService: RatingService,
    private val customerService: CustomerService
){

    @GetMapping
    @Operation(summary = "Obtener todos los jobs de un customer")
    fun getJobsByCustomerId(
        @Parameter(description = "Id del customer")
        @RequestBody id: Long) = jobService.getJobsByCustomer(id)

    @GetMapping("/{id}")
    @Operation(summary = "Buscar jobs por filtro")
    fun getJobsByParameters(
        @PathVariable id: Long,
        @Parameter(description = "Parametros de busqueda (professional/done/inProgress)")
        @RequestBody parameters: JobSearchParameters) = customerService.getJobsByParameters(id, parameters)

    @PostMapping("/rate")
    @Operation(summary = "Calificar un professional al concluir el job")
    fun rateProfessional(@RequestBody rating: RatingDTO) = ratingService.rateProfessional(rating)



}