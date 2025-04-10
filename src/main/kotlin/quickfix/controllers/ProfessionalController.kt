package quickfix.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.model.Professional
//import quickfix.dto.professional.ProfessionalDTO
import quickfix.services.ProfessionalService

@RestController
@RequestMapping("/professional")
@CrossOrigin = ["*"]

class ProfessionalController () {

    val professionalService : ProfessionalService

    @PostMapping("/")
    fun createProfessional(@RequestBody professional: Professional) =
        professionalService.create(professional)

    @GetMapping("/{id}")
    fun getProfessionalById(@PathVariable id: Long) =
        professionalService.getProfessionalById(id)

}