package quickfix.models

import jakarta.persistence.*
import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

@Entity
@Table(name = "ratings")
class Rating : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @ManyToOne
    lateinit var userFrom: User

    @ManyToOne
    lateinit var userTo: User

    @OneToOne
    lateinit var job: Job
    
    lateinit var yearAndMonth: LocalDate

    var score: Int = 0
    lateinit var comment: String

    override fun validate() {
        if (!validScore(score)) throw BusinessException("El puntaje no puede ser mayor a 5 o menor a 1")
        if (!validDate(yearAndMonth)) throw BusinessException("Fecha fuera de rango válido")
        if (!validComment(comment)) throw BusinessException("Debe agregar un comentario")
        if (!jobIncludesUsers()) throw BusinessException("El job a valorar no contiene a uno/ambos usuarios")
        if (userFrom.id == userTo.id) throw BusinessException("Un usuario no puede calificarse a sí mismo")
    }

    private fun jobIncludesUsers(): Boolean = setOf(job.customer.id, job.professional.id).containsAll(setOf(userFrom.id, userTo.id))

    private fun validComment(comment: String): Boolean = comment.isNotBlank()

    private fun validDate(ratingDate: LocalDate): Boolean = ratingDate >= job.date

    private fun validScore(score: Int): Boolean = score in 1..5
}