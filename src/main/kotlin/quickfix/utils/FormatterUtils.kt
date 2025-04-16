package quickfix.utils

import quickfix.utils.exceptions.BusinessException
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

fun stringifyDate(fecha: YearMonth): String = fecha.format(formatter)
fun datifyString(fechaStr: String): YearMonth {
    return try {
        YearMonth.parse(fechaStr, formatter)
    } catch (e: DateTimeParseException) {
        throw BusinessException("La fecha no tiene el formato esperado: yyyy-MM")
    }
}
