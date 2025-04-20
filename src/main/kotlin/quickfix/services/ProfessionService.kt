package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.ProfessionRepository
import quickfix.models.Profession
import quickfix.utils.exceptions.BusinessException


@Service
class ProfessionService(
    private val professionRepository: ProfessionRepository
){
    fun getProfessionByName(profession : String) : Profession {
        return professionRepository.findByName(profession).orElseThrow { BusinessException("la profession con id ${profession} no existe.") }
    }
    //fun getProfessionByType(profession: ProfessionType) : Profession =
    //    professionRepository.findByProfessionType(profession)
    //    ?: throw BusinessException("No se encontró la profesión")
}