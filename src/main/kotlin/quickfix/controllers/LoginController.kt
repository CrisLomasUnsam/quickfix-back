package quickfix.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.services.LoginService
import quickfix.dto.login.LoginDTO


@RestController
@RequestMapping("/login")
@CrossOrigin("*")
@Tag(name = "Login")
class LoginController (val loginService: LoginService) {

    @PostMapping("/")
    fun loginCustomer(@RequestBody loginDTO: LoginDTO) =
        loginService.loginUser(loginDTO)


}