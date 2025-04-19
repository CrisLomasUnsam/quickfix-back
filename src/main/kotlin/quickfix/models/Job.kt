package quickfix.models

import jakarta.persistence.*
import quickfix.utils.enums.JobStatus
import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

@Entity
class Job : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @ManyToOne
    lateinit var professional: User

    @ManyToOne
    lateinit var customer: User

    @Column(columnDefinition = "DATE")
    lateinit var date: LocalDate

    var done: Boolean = false

    @OneToOne(cascade = [(CascadeType.ALL)])
    lateinit var profession : Profession

    @Enumerated(EnumType.STRING)
    var status : JobStatus = JobStatus.PENDING

    //Esto va a ser la fecha y hora actual + los minutos definidos en availability
    lateinit var initDateTime: LocalDate

    var price: Double = 0.0

    fun calculateDistance() : Number = -1

    private fun validPrice(): Boolean = this.price > 0.0

    private fun validDate(): Boolean = this.date.isBefore(LocalDate.now()) || this.date.isEqual(LocalDate.now())

    override fun validate() {
        if (!validPrice()) throw BusinessException("El precio debe ser mayor a cero")
        if (!validDate()) throw BusinessException("La fecha no puede ser en el futuro")
    }

}