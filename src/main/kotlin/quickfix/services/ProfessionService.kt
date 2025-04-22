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

    fun assertProfessionExists(professionId: Long) {
        if(!professionRepository.existsById(professionId))
            throw BusinessException("La profesión no existe")
    }

    fun getProfessionById(id: Long): Profession =
        professionRepository.findById(id).orElseThrow{ BusinessException("La profesión no está disponible.") }

    fun getProfessionByName(profession : String) : Profession {
        return professionRepository.findByNameIgnoreCase(profession).orElseThrow {  BusinessException("La profesión no está disponible.")  }
    }

}
