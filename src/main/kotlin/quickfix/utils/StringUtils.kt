package quickfix.utils

import quickfix.models.Job
import quickfix.utils.enums.ProfessionTypes
import quickfix.utils.enums.JobStatus

private val jobStatusMapping = mapOf(
    "finalizados" to JobStatus.DONE,
    "pendientes" to JobStatus.PENDING,
    "cancelados" to JobStatus.CANCELED
)

private val professionMapping = mapOf(
    "gasista" to ProfessionTypes.GASISTA,
    "carpintero" to ProfessionTypes.CARPINTERO,
    "albanil" to ProfessionTypes.ALBANIL,
    "albañil" to ProfessionTypes.ALBANIL,
    "pintor" to ProfessionTypes.PINTOR,
    "jardinero" to ProfessionTypes.JARDINERO,
    "plomero" to ProfessionTypes.PLOMERO,
    "arquitecto" to ProfessionTypes.ARQUITECTO,
    "mecanico" to ProfessionTypes.MECANICO,
    "mecánico" to ProfessionTypes.MECANICO,
    "electricista" to ProfessionTypes.ELECTRICISTA,
    "otros" to ProfessionTypes.OTROS
)

fun hasMatchingStart(a: String, b: String, limit: Int = 5): Boolean {
    val prefixLength = a.zip(b).takeWhile { it.first == it.second }.count()
    return prefixLength >= limit
}

fun matchJobStatusFromString(param: String): JobStatus? {
    val cleanedParam = param.trim().lowercase()
    return jobStatusMapping.entries.find { (key, _) ->
        cleanedParam.contains(key) ||
        key.contains(cleanedParam) ||
        hasMatchingStart(cleanedParam, key)
    }?.value
}

fun matchProfessionFromString(param: String, element: Job): Boolean {
    val cleanedParam = param.trim().lowercase()
    val matched = professionMapping.entries.find { (key, _) ->
        cleanedParam.contains(key) ||
        key.contains(cleanedParam) ||
        hasMatchingStart(cleanedParam, key)
    }?.value
    return matched != null &&
            element.professional.professionalInfo.professions.any {
                it.professionType == matched
            }
}
