package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import quickfix.dto.page.PageDTO
import quickfix.dto.rating.RateUserPageDTO
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

    @PatchMapping("/editRating")
    @Operation(summary = "Modificar una calificación")
    fun editRating(@ModelAttribute("currentUserId") currentUserId : Long, @RequestBody rating: RatingDTO) =
        ratingService.updateRating(currentUserId, rating)

    @GetMapping("/customer/received")
    @Operation(summary = "Obtener calificaciones paginadas recibidas como cliente")
    fun findRatingsReceivedByCustomer(
        @ModelAttribute("currentUserId") currentCustomerId: Long,
        @RequestParam(defaultValue = "0") page: Int
    ): Page<RatingDTO> = ratingService.findRatingsReceivedByCustomer(currentCustomerId, page).map { it.toDTO() }

    @GetMapping("/professional/received")
    @Operation(summary = "Obtener calificaciones paginadas recibidas como profesional")
    fun findRatingsReceivedByProfessional(
        @ModelAttribute("currentUserId") currentProfessionalId: Long,
        @RequestParam(defaultValue = "0") page: Int
    ): Page<RatingDTO> = ratingService.findRatingsReceivedByProfessional(currentProfessionalId, page).map { it.toDTO() }

    @GetMapping("/professional/jobRating/{jobId}")
    @Operation(summary = "Obtener la calificación realizada a un cliente en un trabajo en particular")
    fun findJobRatingAsProfessional(@ModelAttribute("currentProfessionalId") currentProfessionalId: Long, @PathVariable jobId: Long): RateUserPageDTO =
        ratingService.getJobRatingAsProfessional(currentProfessionalId, jobId)

    @GetMapping("/customer/jobRating/{jobId}")
    @Operation(summary = "Obtener la calificación realizada a un profesional en un trabajo en particular")
    fun findJobRatingAsCustomer(@ModelAttribute("currentCustomerId") currentCustomerId: Long, @PathVariable jobId: Long): RateUserPageDTO =
        ratingService.getJobRatingAsCustomer(currentCustomerId, jobId)

    @GetMapping("/seeCustomerRatings/{customerId}")
    @Operation(summary = "Ver sección de ratings de un cliente")
    fun seeCustomerRatings(
        @PathVariable("customerId") customerId: Long,
        @RequestParam(defaultValue = "0") ratingValue: Int,
        @RequestParam(defaultValue = "0") pageNumber: Int) : PageDTO<RatingDTO> =
        PageDTO.toRatingsDTO(ratingService.findCustomerRatings(customerId, pageNumber, ratingValue))

    @GetMapping("/seeProfessionalRatings/{professionalId}")
    @Operation(summary = "Ver sección de ratings de un profesional")
    fun seeProfessionalRatings(
        @PathVariable("professionalId") professionalId: Long,
        @RequestParam(defaultValue = "0") ratingValue: Int,
        @RequestParam(defaultValue = "0") pageNumber: Int) : PageDTO<RatingDTO> =
        PageDTO.toRatingsDTO(ratingService.findProfessionalRatings(professionalId, pageNumber, ratingValue))

}