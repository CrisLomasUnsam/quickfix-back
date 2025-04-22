package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.ProfessionRepository
import quickfix.models.Profession
import quickfix.utils.exceptions.BusinessException


@Service
class ProfessionService(
    private val professionRepository: ProfessionRepository
){

    fun getById(id: Long) : Profession =
        professionRepository.findById(id).orElseThrow { throw BusinessException() }

    fun getByNameIgnoreCase(profession : String) : Profession =
        professionRepository.findByNameIgnoreCase(profession) ?: throw BusinessException("Profesión no encontrada")

}