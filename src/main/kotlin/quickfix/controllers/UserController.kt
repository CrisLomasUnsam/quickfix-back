package quickfix.controllers

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import quickfix.services.UserService


@RestController
@RequestMapping("/user")
@CrossOrigin("*")
class UserController(val userService: UserService) {

    //@GetMapping("/data/{id}")
    //fun userInfo(@PathVariable id: Int) : UserDTO = UserDTO.toDTO(userService.findById(id))

}