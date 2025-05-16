package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.register.RegisterRequestDTO
import quickfix.services.RegisterService

@RestController
@RequestMapping("/registration")
@Tag(name = "Registro", description = "Operaciones para registrar nuevos usuarios")

class RegisterController(
    private val registerService: RegisterService,
){

    @PostMapping
    @Operation(summary = "Registrar usuario", description = "Registra un usuario")
    fun registerUser(@RequestBody registerData: RegisterRequestDTO) {
        registerService.registerUser(registerData)
    }

    @GetMapping("/confirm/{token}")
    @Operation(summary = "Confirmar registro de usuario por link")
    fun confirmRegistration(@PathVariable token: String) {
        registerService.validateUserByToken(token)
    }
}
