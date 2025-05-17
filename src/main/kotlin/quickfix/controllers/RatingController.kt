package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import quickfix.dto.rating.RatingDTO
import quickfix.dto.rating.toDTO
import quickfix.services.RatingService

@RestController
@RequestMapping("/rating")
@Tag(name = "Calificaciones", description = "Operaciones relacionadas a la calificación entre usuarios")
class RatingController(val ratingService: RatingService) {

    @ModelAttribute("currentUserId")
    fun getCurrentUserId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @PostMapping("/rateUser")
    @Operation(summary = "Calificar un usuario al concluir el job")
    fun rateUser(@ModelAttribute("currentUserId") currentUserId : Long, @RequestBody rating: RatingDTO) =
        ratingService.rateUser(currentUserId, rating)

    @GetMapping("/jobRating/{jobId}")
    @Operation(summary = "Obtener calificacion de un recibidas por un usuario")
    fun getJobRating(@ModelAttribute("currentUserId") currentUserId: Long, @PathVariable jobId: Long)=
        ratingService.jobRating(currentUserId, jobId)

    @GetMapping("/received")
    @Operation(summary = "Obtener calificaciones paginadas recibidas por un usuario")
    fun findRatingsReceivedByUser(
        @ModelAttribute("currentUserId") currentUserId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): Page<RatingDTO> = ratingService.findRatingsReceivedByUser(
        currentUserId,
        PageRequest.of(page, size, Sort.Direction.ASC, "yearAndMonth"))
        .map { it.toDTO() }

    @GetMapping("/madeBy")
    @Operation(summary = "Obtener calificaciones hechas por un usuario")
    fun findRatingsMadeByUser(@ModelAttribute("currentUserId") currentUserId: Long): List<RatingDTO> =
        ratingService.findRatingsMadeByUser(currentUserId).map { it.toDTO() }

    @PatchMapping("/edit")
    @Operation(summary = "Editar una calificación")
    fun updateRating(@ModelAttribute("currentUserId") currentUserId: Long, @RequestBody data: RatingDTO) =
        ratingService.updateRating(currentUserId, data)

}