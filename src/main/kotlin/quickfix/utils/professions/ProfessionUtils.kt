package quickfix.utils.professions

import quickfix.models.Job
import quickfix.utils.functions.hasMatchingStart

fun matchProfessionFromString(param: String, job: Job): Boolean {
    val cleanedParam = param.trim().lowercase()
    val matched = ProfessionNames.find { profession ->
        cleanedParam.contains(profession) || profession.contains(cleanedParam) || hasMatchingStart(cleanedParam, profession)
    }
    return matched != null &&
            job.professional.professionalInfo.hasProfessionByName(matched)
}