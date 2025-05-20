package quickfix.utils.jobs

import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.models.Job
import quickfix.models.TimeUnit
import quickfix.utils.enums.JobStatus
import quickfix.utils.functions.hasMatchingStart
import java.time.LocalDateTime

fun dateTimesCollides(startDt1 : LocalDateTime, endDt1 : LocalDateTime, startDt2 : LocalDateTime, endDt2 : LocalDateTime) : Boolean =
    startDt1 < endDt2 && startDt2 < endDt1

fun getJobEndtime(job : Job) : LocalDateTime {
    val jobDuration = job.duration * TimeUnit.fromLabel(job.durationUnit)!!.minutes
    return job.initDateTime.plusMinutes(jobDuration)
}

fun getJobOfferEndtime(jobOffer : JobOfferDTO) : LocalDateTime {
    val jobDuration = jobOffer.jobDuration * TimeUnit.fromLabel(jobOffer.jobDurationTimeUnit)!!.minutes
    return jobOffer.neededDatetime.plusMinutes(jobDuration)
}

fun matchJobStatusFromString(param: String): JobStatus? {
    val cleanedParam = param.trim().lowercase()
    return jobStatusMapping.entries.find { (key, _) ->
        cleanedParam.contains(key) ||
                key.contains(cleanedParam) ||
                hasMatchingStart(cleanedParam, key)
    }?.value
}

private val jobStatusMapping = mapOf(
    "finalizados" to JobStatus.DONE,
    "pendientes" to JobStatus.PENDING,
    "en curso" to JobStatus.IN_PROGRESS,
    "cancelados" to JobStatus.CANCELED
)