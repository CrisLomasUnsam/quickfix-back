package quickfix.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.user.UserDTO
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.services.UserService

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = ["*"])
@Tag(name = "Usuarios", description = "Operaciones realizadas desde un usuario no profesional")
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

}