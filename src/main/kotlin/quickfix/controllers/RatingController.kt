package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.rating.RatingDTO
import quickfix.dto.rating.toDTO
import quickfix.services.RatingService

@RestController
@RequestMapping("/rating")
@CrossOrigin(origins = ["*"])
@Tag(name = "Operaciones relacionadas a la calificación entre usuarios")
class RatingController(val ratingService: RatingService) {

    @PostMapping("/rateUser")
    @Operation(summary = "Calificar un usuario al concluir el job")
    fun rateUser(@RequestBody rating: RatingDTO) =
        ratingService.rateUser(rating)

    @GetMapping("/received/{id}")
    @Operation(summary = "Obtener calificaciones recibidas por un usuario")
    fun findRatingsReceivedByUser(@PathVariable id: Long): List<RatingDTO> = ratingService.findRatingsReceivedByUser(id).map { it.toDTO() }

    @GetMapping("madeBy/{id}")
    @Operation(summary = "Obtener calificaiones hechas por un usuario")
    fun findRatingsMadeByUser(@PathVariable id: Long): List<RatingDTO> = ratingService.findRatingsMadeByUser(id).map { it.toDTO() }

}