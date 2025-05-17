package quickfix.bootstrap.builders

import quickfix.models.Profession
import quickfix.utils.professions.ProfessionNames

class ProfessionBuilder {
    companion object {
        fun buildProfessions() : List<Profession> {
            return ProfessionNames.map { Profession().apply {name = it} }
        }
    }
}