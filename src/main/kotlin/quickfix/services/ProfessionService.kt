package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.ProfessionRepository
import quickfix.models.Profession
import quickfix.utils.exceptions.NotFoundException

@Service
class ProfessionService(
    private val professionRepository: ProfessionRepository
){

    fun assertProfessionExists(professionId: Long) {
        if(!professionRepository.existsById(professionId))
            throw NotFoundException("La profesión no existe")
    }

    fun getProfessionById(id: Long): Profession =
        professionRepository.findById(id).orElseThrow{ NotFoundException("La profesión no está disponible.") }

    fun getByNameIgnoreCase(profession : String) : Profession {
        return professionRepository.findByNameIgnoreCase(profession).orElseThrow {  NotFoundException("La profesión no está disponible.")  }
    }

}
