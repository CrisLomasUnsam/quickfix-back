package quickfix.dto.professional

import quickfix.models.ProfessionalProfession

data class ProfessionDTO(
    val id: Long,
    val name: String,
    val active: Boolean
) {
    companion object {
        fun toDto(professionalProfession: ProfessionalProfession) : ProfessionDTO =
            ProfessionDTO(
                id = professionalProfession.profession.id,
                name = professionalProfession.profession.name,
                active = professionalProfession.active
            )
    }
}