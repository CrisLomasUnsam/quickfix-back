package quickfix.utils.jobs

import quickfix.utils.enums.JobStatus
import quickfix.utils.functions.hasMatchingStart

private val jobStatusMapping = mapOf(
    "finalizados" to JobStatus.DONE,
    "pendientes" to JobStatus.PENDING,
    "cancelados" to JobStatus.CANCELED
)

fun matchJobStatusFromString(param: String): JobStatus? {
    val cleanedParam = param.trim().lowercase()
    return jobStatusMapping.entries.find { (key, _) ->
        cleanedParam.contains(key) ||
                key.contains(cleanedParam) ||
                hasMatchingStart(cleanedParam, key)
    }?.value
}
