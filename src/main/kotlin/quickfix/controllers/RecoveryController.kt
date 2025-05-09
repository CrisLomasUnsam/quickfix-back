package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.services.UserService

@RestController
@RequestMapping("/recovery")
@Tag(name = "Cambiar la contraseña")
class RecoveryController(
    private val userService: UserService
) {

    @PostMapping
    @Operation(summary = "Enviar un mail de recuperación al usuario")
    fun requestUpdateUserPassword(@RequestBody mail : String) =
        userService.changeUserPassword(mail)

    @GetMapping("/confirm")
    @Operation(summary = "Confirmar cambio de contraseña")
    fun confirmPasswordChange(@RequestParam token: String) {
        userService.validateUserByToken(token)
    }
}