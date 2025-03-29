package services

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EjemploController {

    @GetMapping("/ejemplo/")
    fun defaultGet(): String = "hola"

}