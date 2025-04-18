package quickfix.utils

import quickfix.models.Job
import quickfix.models.Professions
import quickfix.utils.enums.JobStatus

private val jobStatusMapping = mapOf(
    "finalizados" to JobStatus.DONE,
    "pendientes" to JobStatus.PENDING,
    "cancelados" to JobStatus.CANCELED
)

private val professionMapping = mapOf(
    "gasista" to Professions.GASISTA,
    "carpintero" to Professions.CARPINTERO,
    "albanil" to Professions.ALBANIL,
    "albañil" to Professions.ALBANIL,
    "pintor" to Professions.PINTOR,
    "jardinero" to Professions.JARDINERO,
    "plomero" to Professions.PLOMERO,
    "arquitecto" to Professions.ARQUITECTO,
    "mecanico" to Professions.MECANICO,
    "mecánico" to Professions.MECANICO,
    "electricista" to Professions.ELECTRICISTA,
    "otros" to Professions.OTROS
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
                it.name.equals(matched.name, true)
            }
}
