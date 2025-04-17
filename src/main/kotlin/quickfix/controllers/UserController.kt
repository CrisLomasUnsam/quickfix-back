package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.JobOfferDTO
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.user.UserDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.services.UserService

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = ["*"])
@Tag(name = "Operaciones relacionadas a la información de los usuarios")

class UserController(
    val userService: UserService
) {

    @GetMapping("/data/{id}")
    fun userInfo(@PathVariable id: Long) : UserDTO =
        UserDTO.toDTO(userService.getUserById(id))

    @PutMapping("/data/edit/{id}")
    fun updateUserInfo(@PathVariable id: Long, @RequestBody modifiedInfo: UserModifiedInfoDTO) =
        userService.changeUserInfo(id,modifiedInfo)

    /*************************
    JOB REQUEST METHODS
     **************************/

    @PostMapping("/requestJob")
    @Operation(summary = "Buscar profesionales disponibles para el job seleccionado por el customer")
    fun requestJob(@RequestBody jobRequest : JobRequestDTO) =
        userService.requestJob(jobRequest)

    @DeleteMapping("/requestJob/{professionId}/{customerId}")
    @Operation(summary = "Cancela el job request")
    fun cancelJobReq(@PathVariable professionId : Long, @PathVariable customerId : Long) = userService.cancelJobReq(professionId, customerId)

    /*************************
    JOB OFFER METHODS
     **************************/

    @GetMapping("/jobOffers/{customerId}")
    @Operation(summary = "Utilizado para el polling que devuelve las nuevas ofertas enviadas por los profesionales")
    fun getJobOffers(@PathVariable customerId: Long) : Set<JobOfferDTO> =
        userService.getJobOffers(customerId)

}