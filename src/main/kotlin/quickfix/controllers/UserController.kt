package quickfix.controllers

import org.springframework.web.bind.annotation.*
import quickfix.models.User
import quickfix.services.UserService

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = ["*"])

class UserController (val userService: UserService) {

    @PostMapping("/user")
    fun createUser(@RequestBody user : User) = userService.createUser(user)

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id : Int) = userService.getUserById(id)
}