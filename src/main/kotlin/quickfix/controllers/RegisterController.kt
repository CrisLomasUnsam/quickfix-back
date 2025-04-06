package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.register.RegisterRequestDTO
import quickfix.services.RegisterService

@RestController
@RequestMapping("/registration")
@CrossOrigin("*")
@Tag(name = "Registro", description = "Operaciones para registrar nuevos customer/professional")

class RegisterController(
    val registerService: RegisterService
){

    @PostMapping("/customer")
    @Operation(summary = "Registrar customer", description = "Registra un usuario con rol de customer")
    fun registerCustomer(@RequestBody registerData: RegisterRequestDTO) =
        registerService.registerCustomer(registerData)

    @PostMapping("/professional")
    @Operation(summary = "Registrar professional", description = "Registra un usuario con rol de professional")
    fun registerProfessional(@RequestBody registerData: RegisterRequestDTO) =
        registerService.registerProfessional(registerData)

}
