package quickfix.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import quickfix.dto.user.UserDTO
import quickfix.dto.user.UserModifiedInfoDTO
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
        UserDTO.toDTO(userService.getUserById(currentUserId))

    @GetMapping("/img")
    fun userImg(@ModelAttribute("currentUserId") currentUserId : Long) : ByteArray =
        userService.getUserById(currentUserId).avatar

    @PatchMapping("/data/edit")
    fun updateUserInfo(@ModelAttribute("currentUserId") currentUserId : Long, @RequestBody modifiedInfo: UserModifiedInfoDTO) =
        userService.changeUserInfo(currentUserId, modifiedInfo)

    @PatchMapping("/avatar")
    fun updateAvatar(@ModelAttribute("currentUserId") currentUserId: Long, @RequestParam("file") file: MultipartFile) =
        userService.updateAvatar(currentUserId, file)
    @GetMapping("/avatar")
    fun getAvatar(@ModelAttribute("currentUserId") currentUserId: Long) = userService.getAvatar(currentUserId)

}