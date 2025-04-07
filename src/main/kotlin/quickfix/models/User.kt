package quickfix.models

import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

interface User : Id {

    companion object {
        const val EDAD_REQUERIDA = 18
    }

    var info : UserInfo

    override fun validate() = info.validate()

}