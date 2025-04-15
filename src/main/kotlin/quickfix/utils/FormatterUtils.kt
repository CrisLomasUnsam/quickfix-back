package quickfix.utils

import java.time.YearMonth
import java.time.format.DateTimeFormatter

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
val ISOformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

fun stringifyDate(fecha: YearMonth): String = fecha.format(formatter)
fun datifyString(fechaStr: String): YearMonth = YearMonth.parse(fechaStr, ISOformatter)