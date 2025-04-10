package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.JobSearchRequestDTO
import quickfix.dto.rating.RatingDTO
import quickfix.dto.user.UserDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.services.JobService
import quickfix.services.ProfessionalService
import quickfix.services.RatingService
import quickfix.services.UserService

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = ["*"])
@Tag(name = "Operaciones relacionadas a la información de los usuarios")

class UserController (val userService: UserService, private val jobService: JobService, private val ratingService: RatingService,  private val professionalService : ProfessionalService) {

    @GetMapping("/data/{id}")
    fun userInfo(@PathVariable id: Long) : UserDTO = UserDTO.toDTO(userService.getUserInfoById(id)!!)

    @PutMapping("/data/edit/{id}")
    fun updateUserInfo(@PathVariable id: Long, @RequestBody modifiedInfo: UserModifiedInfoDTO) = userService.changeUserInfo(id,modifiedInfo)

    @GetMapping("/jobs/{id}")
    @Operation(summary = "Obtener todos los jobs de un customer")
    fun getJobsByCustomerId (@Parameter(description = "Id del customer") @PathVariable id: Long) = jobService.getJobsByCustomer(id)

    @GetMapping("/filterJobs/{id}/{parameter}")
    @Operation(summary = "Buscar jobs por filtro")
    fun getJobsByParameters(
        @PathVariable id: Long,
        @Parameter(description = "Parametro de busqueda: string")
        @PathVariable parameter: String) = userService.getJobsByParameter(id, parameter)

    @PostMapping("/rateProfessional")
    @Operation(summary = "Calificar un professional al concluir el job")
    fun rateProfessional(@RequestBody rating: RatingDTO) = ratingService.rateProfessional(rating)

    @PostMapping("/jobs/search")
    @Operation(summary = "Buscar profesionales disponibles para el job seleccionado por el customer")
    fun pushJobRequest(@RequestBody jobSearchRequestDTO : JobSearchRequestDTO) = professionalService.jobRequest(jobSearchRequestDTO)


}