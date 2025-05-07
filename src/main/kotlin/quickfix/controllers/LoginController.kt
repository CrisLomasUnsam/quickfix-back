package quickfix.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import quickfix.dto.login.LoginDTO
import quickfix.services.LoginService

@RestController
@RequestMapping("/login")
@Tag(name = "Login")
class LoginController (
    private val loginService: LoginService,
) {
    @PostMapping("/customer")
    fun loginAsCustomer(@RequestBody loginDTO: LoginDTO) =
        loginService.loginAsCustomer(loginDTO)

    @PostMapping("/professional")
    fun loginAsProfessional(@RequestBody loginDTO: LoginDTO) =
        loginService.loginAsProfessional(loginDTO)
}