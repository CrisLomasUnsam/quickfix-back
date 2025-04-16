package quickfix.utils.converters

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import quickfix.utils.datifyString
import quickfix.utils.stringifyDate
import java.time.YearMonth

@Converter
class YearMonthConverter : AttributeConverter<YearMonth, String> {
    override fun convertToDatabaseColumn(attribute: YearMonth): String = stringifyDate(attribute)
    override fun convertToEntityAttribute(dbData: String): YearMonth =  datifyString(dbData)
}