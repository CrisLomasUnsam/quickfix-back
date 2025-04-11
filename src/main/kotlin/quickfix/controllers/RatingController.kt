package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.rating.RatingDTO
import quickfix.services.RatingService

@RestController
@RequestMapping("/rating")
@CrossOrigin(origins = ["*"])
@Tag(name = "Operaciones relacionadas a la calificación entre usuarios")
class RatingController(val ratingService: RatingService) {

    @PostMapping("/rateUser")
    @Operation(summary = "Calificar un usuario al concluir el job")
    fun rateUser(@RequestBody rating: RatingDTO) = ratingService.rateUser(rating)
}