package quickfix.utils.searchParameters

import quickfix.models.Job
import quickfix.utils.hasMatchingStart

data class JobSearchParameters (

    private val parameter: String

) : ISearchParameters<Job> {

    override fun matches(element: Job): Boolean {
        return when (parameter.trim().lowercase()) {
            "finalizado" -> element.done
            "finalizados" -> element.done
            "pendiente" -> element.inProgress
            "pendientes" -> element.inProgress
            else -> {
                val parameter = parameter.trim().lowercase()
                return element.professional.professionalInfo.professions.any {
                    val name = it.name.lowercase()
                    name.contains(parameter) ||
                    name.startsWith(parameter) ||
                    parameter.startsWith(name) ||
                    hasMatchingStart(name, parameter)
                }
            }
        }
    }
}

