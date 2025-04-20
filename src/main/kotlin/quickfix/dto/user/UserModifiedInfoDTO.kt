package quickfix.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.dto.address.AddressDTO
import quickfix.dto.job.JobRequestDTO
import quickfix.models.Address
import quickfix.models.Gender
import quickfix.utils.DateWithDayFormatter
import quickfix.utils.datifyString
import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

@Schema(description = "actualiza datos del User")
data class UserModifiedInfoDTO(
    var mail: String?,
    var name: String?,
    var lastName: String?,
    var dateBirth: String?,
    var gender: Gender?,
    var address: AddressDTO?
)