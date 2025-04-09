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

    @PostMapping
    @Operation(summary = "Registrar usuario", description = "Registra un usuario y le asigna dos perfiles: uno para cada uno de los dos roles (Customer y Professional)")
    fun registerUser(@RequestBody registerData: RegisterRequestDTO) =  registerService.registerUser(registerData)


}
