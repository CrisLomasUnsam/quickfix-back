package quickfix.utils.functions

import quickfix.utils.exceptions.IllegalDataException
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val YearAndMonthformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy")
val DateWithDayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
val CustomDateTimeWithoutYearFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm")

fun stringifyDate(date: LocalDate, format: DateTimeFormatter): String = date.format(format)

fun stringifyDateTimeWithoutYear(dateTime: LocalDateTime): String = dateTime.format(CustomDateTimeWithoutYearFormatter)

fun dateTimeFromTimestamp(timestamp : Long) : LocalDateTime =
    Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()

fun datifyStringWithDay(dateStr: String): LocalDate {
    return try {
        LocalDate.parse(dateStr, DateWithDayFormatter)
    } catch (e: DateTimeParseException) {
        throw IllegalDataException("La fecha no tiene el formato esperado dd/MM/yyyy")
    }
}

fun datifyStringMonthAndYear(fechaStr: String): LocalDate {
    return try {
        val date = YearMonth.parse(fechaStr, YearAndMonthformatter)
        date.atDay(1)
    } catch (e: DateTimeParseException) {
        throw IllegalDataException("La fecha no tiene el formato esperado MM/yyyy")
    }
}
