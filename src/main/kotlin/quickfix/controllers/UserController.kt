package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import quickfix.dao.UserRepository
import quickfix.dto.rating.UserProfileDTO
import quickfix.dto.user.UserDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.UserProfileProjectionDTO
import quickfix.services.UserService
import quickfix.utils.exceptions.BusinessException

@RestController
@RequestMapping("/user")
@Tag(name = "Usuarios", description = "Operaciones realizadas desde un usuario no profesional")
class UserController(
    val userService: UserService,
    val userRepository: UserRepository
) {

    @ModelAttribute("currentUserId")
    fun getCurrentUserId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @GetMapping("/data")
    fun userInfo(@ModelAttribute("currentUserId") currentUserId : Long) : UserDTO =
        UserDTO.toDTO(userService.getById(currentUserId))

    @GetMapping("/img")
    fun userImg(@ModelAttribute("currentUserId") currentUserId : Long) : ByteArray =
        userService.getById(currentUserId).avatar

    @PatchMapping("/data/edit")
    fun updateUserInfo(@ModelAttribute("currentUserId") currentUserId : Long, @RequestBody modifiedInfo: UserModifiedInfoDTO) =
        userService.changeUserInfo(currentUserId, modifiedInfo)

    @PatchMapping("/avatar")
    fun updateAvatar(@ModelAttribute("currentUserId") currentUserId: Long, @RequestParam("file") file: MultipartFile) =
        userService.updateAvatar(currentUserId, file)
        
    @GetMapping("/avatar")
    fun getAvatar(@ModelAttribute("currentUserId") currentUserId: Long) = 
        userService.getAvatar(currentUserId)

    @GetMapping("/seeProfile")
    @Operation(summary = "Ver perfil de usuario ccalificacion y cantidad de trabajos terminados",)
    fun getSeeProfile(@ModelAttribute("currentUserId") currentUserId: Long): UserProfileProjectionDTO {
        return userRepository.getUserProfileData(currentUserId)
    }
}
