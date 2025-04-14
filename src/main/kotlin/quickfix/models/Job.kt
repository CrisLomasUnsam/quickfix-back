package quickfix.models

import jakarta.persistence.*
import quickfix.utils.enums.JobStatus
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

    @OneToOne(cascade = [(CascadeType.ALL)])
    lateinit var profession : Profession

    @Enumerated(EnumType.STRING)
    var status : JobStatus = JobStatus.PENDING

    //Esto va a ser la fecha y hora actual + los minutos definidos en availability
    lateinit var initDateTime: LocalDate

    var price: Double = 0.0

    fun calculateDistance() : Number = -1

    override fun validate() {
        if (price <= 0) throw BusinessException("El precio debe ser mayor a cero")
    }

}