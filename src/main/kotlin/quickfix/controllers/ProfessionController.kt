package quickfix.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.websocket.server.PathParam
import org.springframework.web.bind.annotation.*

import quickfix.services.ProfessionService
import quickfix.services.ProfessionalService

@RestController
@RequestMapping("/profession")
@CrossOrigin(origins = ["*"])
@Tag(name = "Profesiones", description = "Operaciones realizadas desde una profession")
class ProfessionController(
    private val professionService: ProfessionService
) {

    @GetMapping("/filter")
    @Operation(summary = "Buscar profession por filtro")
    fun getProfessionByParameters(
        @Parameter(description = "Parametro de busqueda: string")
        @RequestParam parameter: String?) = professionService.getProfessionByParameter(parameter)

}