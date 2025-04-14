package quickfix.utils.searchParameters

import quickfix.models.Job
import quickfix.utils.enums.JobStatus
import quickfix.utils.hasMatchingStart

data class JobSearchParameters (

    private val parameter: String

) : ISearchParameters<Job> {

    override fun matches(element: Job): Boolean {
        return when (parameter.trim().lowercase()) {
            "finalizado" -> element.status == JobStatus.DONE
            "finalizados" -> element.status == JobStatus.DONE
            "pendiente" -> element.status == JobStatus.PENDING
            "pendientes" -> element.status == JobStatus.PENDING
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

