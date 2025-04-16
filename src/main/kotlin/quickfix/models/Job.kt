package quickfix.models

import jakarta.persistence.*
import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

@Entity
class Job : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @OneToOne(cascade = [CascadeType.ALL])
    lateinit var professional: User

    @OneToOne(cascade = [CascadeType.ALL])
    lateinit var customer: User

    @Column(columnDefinition = "DATE")
    lateinit var date: LocalDate

    var done: Boolean = false

    val inProgress: Boolean = !done

    var canceled: Boolean = false

    var price: Double = 0.0

    lateinit var distance : Number

    private fun validPrice(): Boolean = this.price > 0.0

    private fun validDate(): Boolean = this.date.isBefore(LocalDate.now()) || this.date.isEqual(LocalDate.now())

    override fun validate() {
        if (!validPrice()) throw BusinessException("El precio debe ser mayor a cero")
        if (!validDate()) throw BusinessException("La fecha no puede ser en el futuro")
    }

}