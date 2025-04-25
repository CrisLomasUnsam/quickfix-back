package quickfix.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.login.LoginDTO
import quickfix.services.LoginService

@RestController
@RequestMapping("/login")
@Tag(name = "Login")
class LoginController (
    private val loginService: LoginService,
) {
    @PostMapping
    fun login(@RequestBody loginDTO: LoginDTO) =
        loginService.login(loginDTO)
}