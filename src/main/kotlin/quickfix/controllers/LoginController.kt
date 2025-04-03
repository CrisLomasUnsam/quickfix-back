package quickfix.controllers

import org.springframework.web.bind.annotation.*
import quickfix.services.LoginService

//import quickfix.dto.login
//import quickfix.services.LoginService

@RestController
@RequestMapping("/Login")
@CrossOrigin("*")

class LoginController (val loginService: LoginService) {

    @PostMapping("/login")
    fun login(@RequestBody loginDTO: LoginDTO) : LoginAcceptedDTO {
        val (email, password) = loginDTO
        return LoginAcceptedDTO.toDTO(email,password)
    }

}