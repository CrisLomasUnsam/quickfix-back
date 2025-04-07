package quickfix.models

import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

class Job : Id {

    override var id: Long = -1
    lateinit var professional: Professional
    lateinit var customer: Customer
    lateinit var date: LocalDate
    var done: Boolean = false
    var canceled: Boolean = false
    var price: Double = 0.0
    lateinit var distance : Number

    override fun validate() {
        if (price <= 0) throw BusinessException("Price must be greater than zero")
    }

    val inProgress: Boolean = !done
}