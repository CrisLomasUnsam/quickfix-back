package controllers

import dto.register.*
import org.springframework.web.bind.annotation.*
import services.RegisterService

@RestController
@RequestMapping("/registration")
@CrossOrigin("*")

class RegisterController(
    val registerService: RegisterService){

    @PostMapping("/customer")
    fun registerCustomer(@RequestBody registerData: CustomerRegisterRequestDTO) = registerService.registerCustomer(registerData)

    @PostMapping("/professional")
    fun registerProfessional(@RequestBody registerData: ProfessionalRegisterRequestDTO) = registerService.registerProfessional(registerData)

}
