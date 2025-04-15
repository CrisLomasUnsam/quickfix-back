package quickfix.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
val ISOformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

fun stringifyDate(fecha: LocalDate): String = fecha.format(formatter)
fun datifyString(fechaStr: String): LocalDate = LocalDate.parse(fechaStr, ISOformatter)