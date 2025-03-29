package controllers

import dto.register.RegisterRequestDTO
import org.springframework.web.bind.annotation.*
import services.RegisterService

@RestController
@RequestMapping("/registration")
@CrossOrigin("*")

class RegisterController(
    val registerService: RegisterService){

    @PostMapping()
    fun registerUser(@RequestBody registerData: RegisterRequestDTO)/*: RegisterResponseDTO */ = registerService.registerUser(registerData)
}
