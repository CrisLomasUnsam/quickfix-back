package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.ProfessionRepository
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException


@Service
class ProfessionService(
    private val professionRepository: ProfessionRepository
){
    fun getProfessionById(id: Long): Profession =
        professionRepository.findById(id).orElseThrow{ BusinessException("Usuario no encontrado") }

    fun getProfessionByName(profession : String) : Profession {
        return professionRepository.findByName(profession).orElseThrow {  BusinessException("profession no encontrada")  }
    }
    //fun getProfessionByType(profession: ProfessionType) : Profession =
    //    professionRepository.findByProfessionType(profession)
    //    ?: throw BusinessException("No se encontró la profesión")
}