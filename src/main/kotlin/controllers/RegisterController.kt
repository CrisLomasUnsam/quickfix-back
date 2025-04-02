package controllers

import dto.register.RegisterRequestDTO
import org.springframework.web.bind.annotation.*
import services.RegisterService

@RestController
@RequestMapping("/registration")
@CrossOrigin("*")

class RegisterController(
    val registerService: RegisterService){

    @PostMapping("/user")
    fun registerCustomer(@RequestBody registerData: RegisterRequestDTO)/*: RegisterResponseDTO */ = registerService.registerCustomer(registerData)

    @PostMapping("/professional")
    fun registerProfessional(@RequestBody registerData: RegisterRequestDTO)/*: RegisterResponseDTO */ = registerService.registerProfessional(registerData)

}
