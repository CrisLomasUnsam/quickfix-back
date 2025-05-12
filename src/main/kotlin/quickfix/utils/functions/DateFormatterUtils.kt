package quickfix.utils.functions

import quickfix.utils.exceptions.BusinessException
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val YearAndMonthformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy")
val DateWithDayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
val CustomDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

fun stringifyDate(date: LocalDate, format: DateTimeFormatter): String = date.format(format)

fun stringifyDateTime(dateTime: LocalDateTime, format: DateTimeFormatter): String = dateTime.format(format)

fun dateTimeFromTimestamp(timestamp : Long) : LocalDateTime =
    Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()

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
