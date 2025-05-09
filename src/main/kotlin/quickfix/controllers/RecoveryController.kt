package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.user.NewCredentialRequestDTO
import quickfix.services.UserService

@RestController
@RequestMapping("/recovery")
@Tag(name = "Contraseña")
class RecoveryController(
    private val userService: UserService
) {

    @PostMapping
    @Operation(summary = "Enviar un mail de recuperación al usuario")
    fun requestUpdateUserPassword(@RequestBody mail : String) =
        userService.changeUserPassword(mail)

    @PatchMapping("/confirm")
    @Operation(summary = "Confirmar cambio de contraseña")
    fun confirmPasswordChange(@RequestBody newCredentials: NewCredentialRequestDTO) {
        userService.validateUserByToken(newCredentials)
    }
}