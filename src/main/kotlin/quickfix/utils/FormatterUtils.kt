package quickfix.utils

import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

fun stringifyDate(fecha: LocalDate): String = fecha.format(formatter)
fun datifyString(fechaStr: String): LocalDate {
    return try {
        LocalDate.parse(fechaStr, formatter)
    } catch (e: DateTimeParseException) {
        throw BusinessException("La fecha no tiene el formato esperado: yyyy-MM-dd")
    }
}

