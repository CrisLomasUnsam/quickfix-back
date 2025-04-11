package quickfix.controllers

import org.springframework.web.bind.annotation.*
import quickfix.services.ProfessionalService

@RestController
@RequestMapping("/professional")
@CrossOrigin (origins = ["*"])

class ProfessionalController (val professionalService : ProfessionalService) {
}