package quickfix.models

import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

class Job : Identifier {

    override var id: Long = -1
    lateinit var professional: User
    lateinit var customer: User
    lateinit var date: LocalDate
    var done: Boolean = false
    val inProgress: Boolean = !done
    var canceled: Boolean = false
    var price: Double = 0.0
    lateinit var distance : Number

    override fun validate() {
        if (price <= 0) throw BusinessException("El precio debe ser mayor a cero")
    }

}