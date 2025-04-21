package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.jobOffer.AcceptedJobOfferDTO
import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobRequest.CancelJobRequestDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
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

    @GetMapping("/img/{id}")
    fun userImg(@PathVariable id: Long) : String =
        userService.getUserById(id).avatar

    @PatchMapping("/data/edit/{id}")
    fun updateUserInfo(@PathVariable id: Long, @RequestBody modifiedInfo: UserModifiedInfoDTO) =
        userService.changeUserInfo(id,modifiedInfo)

    /*************************
    JOB REQUEST METHODS
     **************************/

    @PostMapping("/requestJob")
    @Operation(summary = "Buscar profesionales disponibles para el job seleccionado por el customer")
    fun requestJob(@RequestBody jobRequest : JobRequestDTO) =
        userService.requestJob(jobRequest)

    @DeleteMapping("/requestJob")
    @Operation(summary = "Cancelar un job request")
    fun cancelJobRequest(@RequestBody cancelJobRequest : CancelJobRequestDTO) =
        userService.cancelJobRequest(cancelJobRequest)

    /*************************
    JOB OFFER METHODS
     **************************/

    @GetMapping("/jobOffers/{customerId}")
    @Operation(summary = "Utilizado para el polling que devuelve las nuevas ofertas enviadas por los profesionales")
    fun getJobOffers(@PathVariable customerId: Long) : Set<JobOfferDTO> =
        userService.getJobOffers(customerId)
}