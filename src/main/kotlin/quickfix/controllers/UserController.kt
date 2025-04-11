package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.professional.AvailableProfessionalDTO
import quickfix.dto.user.UserDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.services.UserService

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = ["*"])
@Tag(name = "Operaciones relacionadas a la información de los usuarios")

class UserController (
    val userService: UserService
) {

    @GetMapping("/data/{id}")
    fun userInfo(@PathVariable id: Long) : UserDTO =
        UserDTO.toDTO(userService.getUserInfoById(id)!!)

    @PutMapping("/data/edit/{id}")
    fun updateUserInfo(@PathVariable id: Long, @RequestBody modifiedInfo: UserModifiedInfoDTO) =
        userService.changeUserInfo(id,modifiedInfo)

    @PostMapping("/requestJob")
    @Operation(summary = "Buscar profesionales disponibles para el job seleccionado por el customer")
    fun pushJobRequest(@RequestBody jobRequestDTO : JobRequestDTO) =
        userService.postJobRequest(jobRequestDTO)

    @GetMapping("/availableProfessionals/{customerId}")
    @Operation(summary = "Utilizado para el polling que devuelve las nuevas ofertas enviadas por los profesionales")
    fun getAvailableProfessionals(@PathVariable customerId: Long) : Set<AvailableProfessionalDTO>? = null


}