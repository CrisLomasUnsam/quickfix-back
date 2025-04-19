package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.ProfessionRepository
import quickfix.models.Profession
import quickfix.utils.enums.ProfessionTypes
import quickfix.utils.exceptions.BusinessException


@Service
class ProfessionService(
    private val professionRepository: ProfessionRepository
){
    fun getProfessionByProfessionType(profession: ProfessionTypes) : Profession =
        professionRepository.findByProfessionType(profession)
        ?: throw BusinessException("No se encontró la profesión")
}