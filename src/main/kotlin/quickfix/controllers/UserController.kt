package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import quickfix.dto.user.UserDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.dto.user.SeeUserProfileDTO
import quickfix.services.UserService

@RestController
@RequestMapping("/user")
@Tag(name = "Usuarios", description = "Operaciones realizadas desde un usuario no profesional")
class UserController(
    val userService: UserService
) {

    @ModelAttribute("currentUserId")
    fun getCurrentUserId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @GetMapping("/data")
    fun userInfo(@ModelAttribute("currentUserId") currentUserId : Long) : UserDTO =
        UserDTO.toDTO(userService.getById(currentUserId))

    @PatchMapping("/data/edit")
    fun updateUserInfo(@ModelAttribute("currentUserId") currentUserId : Long, @RequestBody modifiedInfo: UserModifiedInfoDTO) =
        userService.changeUserInfo(currentUserId, modifiedInfo)

    @PatchMapping("/avatar")
    fun updateAvatar(@ModelAttribute("currentUserId") currentUserId: Long, @RequestBody image: MultipartFile) =
        userService.updateAvatar(currentUserId, image)

    @GetMapping("/seeCustomerProfile/{customerId}")
    @Operation(summary = "Ver perfil de cliente: calificacion y cantidad de trabajos terminados",)
    fun getSeeCustomerProfileInfo(@PathVariable customerId: Long): SeeUserProfileDTO =
        SeeUserProfileDTO.fromProjection(userService.getSeeCustomerProfileInfo(customerId))

    @GetMapping("/seeProfessionalProfile/{professionalId}")
    @Operation(summary = "Ver perfil de cliente: calificacion y cantidad de trabajos terminados",)
    fun getSeeProfessionalProfileInfo(@PathVariable professionalId: Long): SeeUserProfileDTO =
        SeeUserProfileDTO.fromProjection(userService.getSeeProfessionalProfileInfo(professionalId))

}
