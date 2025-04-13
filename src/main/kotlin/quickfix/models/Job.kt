package quickfix.models

import jakarta.persistence.*
import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

@Entity
class Job : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @OneToOne(cascade = [(CascadeType.ALL)])
    lateinit var professional: User

    @OneToOne(cascade = [(CascadeType.ALL)])
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