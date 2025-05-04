package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import quickfix.dto.rating.EditRatingDTO
import quickfix.dto.rating.RatingDTO
import quickfix.dto.rating.fromDTO
import quickfix.dto.rating.toDTO
import quickfix.services.RatingService

@RestController
@RequestMapping("/rating")
@Tag(name = "Calificaciones", description = "Operaciones relacionadas a la calificación entre usuarios")
class RatingController(val ratingService: RatingService) {

    @PostMapping("/rateUser")
    @Operation(summary = "Calificar un usuario al concluir el job")
    fun rateUser(@RequestBody rating: RatingDTO) =
        ratingService.rateUser(rating)

    @GetMapping("/received/{id}")
    @Operation(summary = "Obtener calificaciones paginadas recibidas por un usuario")
    fun findRatingsReceivedByUser(
        @PathVariable id: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): Page<RatingDTO> = ratingService.findRatingsReceivedByUser(
        id,
        PageRequest.of(page, size, Sort.Direction.ASC, "yearAndMonth"))
        .map { it.toDTO() }

    @GetMapping("madeBy/{id}")
    @Operation(summary = "Obtener calificaciones hechas por un usuario")
    fun findRatingsMadeByUser(
        @PathVariable id: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): Page<RatingDTO> = ratingService.findRatingsMadeByUser(
        id,
        PageRequest.of(page, size, Sort.Direction.ASC, "yearAndMonth"))
        .map { it.toDTO() }

    @PatchMapping("/edit/{id}")
    @Operation(summary = "Editar una calificación")
    fun updateRating(@PathVariable id: Long, @RequestBody data: EditRatingDTO) = ratingService.updateRating(id, data.fromDTO())
}