package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.user.NewCredentialRequestDTO
import quickfix.services.RegisterService

@RestController
@RequestMapping("/recovery")
@Tag(name = "Recupero de contraseña")
class RecoveryController(
    private val registerService: RegisterService
) {

    @PostMapping
    @Operation(summary = "Enviar un mail de recuperación al usuario")
    fun requestUpdateUserPassword(@RequestBody mail : String) =
        registerService.requestChangeUserPassword(mail)

    @PatchMapping("/confirm")
    @Operation(summary = "Confirmar cambio de contraseña")
    fun confirmPasswordChange(@RequestBody newCredentials: NewCredentialRequestDTO) {
        registerService.recoveryPassword(newCredentials)
    }
}