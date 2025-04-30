package quickfix.utils

import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val YearAndMonthformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy")
val DateWithDayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

fun stringifyDate(fecha: LocalDate, format: DateTimeFormatter): String = fecha.format(format)

fun datifyStringWithDay(fechaStr: String): LocalDate {
    return try {
        LocalDate.parse(fechaStr, DateWithDayFormatter)
    } catch (e: DateTimeParseException) {
        throw BusinessException("La fecha no tiene el formato esperado dd/MM/yyyy")
    }
}

fun datifyStringMonthAndYear(fechaStr: String): LocalDate {
    return try {
        val date = YearMonth.parse(fechaStr, YearAndMonthformatter)
        date.atDay(1)
    } catch (e: DateTimeParseException) {
        throw BusinessException("La fecha no tiene el formato esperado MM/yyyy")
    }
}
