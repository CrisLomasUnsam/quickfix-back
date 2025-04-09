package quickfix.models

import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

class Job : Id {

    override var id: Long = -1
    lateinit var professionalUser: User
    lateinit var customerUser: User
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