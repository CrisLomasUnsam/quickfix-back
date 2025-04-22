package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.ProfessionRepository
import quickfix.models.Profession


@Service
class ProfessionService(
    private val professionRepository: ProfessionRepository
){
    fun getByNameIgnoreCase(profession : String) : Profession = professionRepository.findByNameIgnoreCase(profession) ?: throw BusinessException("Profesión no encontrada")

}