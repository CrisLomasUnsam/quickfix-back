package quickfix.models

import jakarta.persistence.*
import quickfix.utils.MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST
import quickfix.utils.enums.JobStatus
import quickfix.utils.exceptions.JobException
import java.time.LocalDateTime

@Entity
@Table(name = "jobs")
class Job : Identifier {

    @Id @GeneratedValue
    override var id: Long = 0

    @ManyToOne
    lateinit var professional: User

    @ManyToOne
    lateinit var customer: User

    @ManyToOne(cascade = [(CascadeType.MERGE)])
    @JoinColumn(name = "profession_id", nullable = false)
    lateinit var profession : Profession

    @Enumerated(EnumType.STRING)
    var status : JobStatus = JobStatus.PENDING

    lateinit var initDateTime: LocalDateTime
    lateinit var durationUnit : String
    var duration: Int = 0
    var price: Double = 0.0

    @Column(length = 150)
    var description : String = ""

    fun calculateDistance() : Number = -1

    private fun validPrice(): Boolean = this.price > 0

    private fun validDate(): Boolean = this.initDateTime.isAfter(LocalDateTime.now().minusMinutes(MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST))

    override fun validate() {
        if (!validPrice()) throw JobException("El precio debe ser mayor a cero")
        if (!validDate()) throw JobException("No se puede elegir una fecha del pasado")
    }

}