package quickfix.controllers

import org.springframework.web.bind.annotation.*
import quickfix.dto.user.UserDTO
import quickfix.services.UserService

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = ["*"])

class UserController (val userService: UserService) {

    @GetMapping("/data/{id}")
    fun userInfo(@PathVariable id: Long) : UserDTO = UserDTO.toDTO(userService.getUserInfoById(id)!!)
    
}