package quickfix.utils

import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val YearAndMonthformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy")
val DateWithDayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun stringifyDate(fecha: LocalDate, format: DateTimeFormatter): String = fecha.format(format)

fun datifyString(fechaStr: String, format: DateTimeFormatter): LocalDate {
    return try {
        LocalDate.parse(fechaStr, format)
    } catch (e: DateTimeParseException) {
        throw BusinessException("La fecha no tiene el formato esperado")
    }
}

