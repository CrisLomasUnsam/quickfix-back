package quickfix.controllers

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import quickfix.models.User
import quickfix.services.UserService
import quickfix.dto.user.UserDTO

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = ["*"])

class UserController (val userService: UserService) {

    @GetMapping("/data/{id}")
    fun userInfo(@PathVariable id: Long) : UserDTO = UserDTO.toDTO(userService.findById(id))
}