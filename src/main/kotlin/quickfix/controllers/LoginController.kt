package quickfix.controllers

import org.springframework.web.bind.annotation.*
import quickfix.services.LoginService
import quickfix.dto.login.LoginDTO


@RestController
@RequestMapping("/login")
@CrossOrigin("*")

class LoginController (val loginService: LoginService) {

    @PostMapping("/customer")
    fun loginCustomer(@RequestBody loginDTO: LoginDTO) =
        loginService.loginCustomer(loginDTO)

    @PostMapping("/professional")
    fun loginProfessional(@RequestBody loginDTO: LoginDTO) =
        loginService.loginProfessional(loginDTO)
}