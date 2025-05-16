package quickfix.models

import jakarta.persistence.*
import quickfix.utils.exceptions.RatingException
import java.time.LocalDate

@Entity
@Table(name = "ratings",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uq_ratings_job_userfrom",
            columnNames = ["job_id", "user_from_id"]
        )
    ])
class Rating : Identifier {

    @Id @GeneratedValue
    override var id: Long = 0

    @ManyToOne
    lateinit var userFrom: User

    @ManyToOne
    lateinit var userTo: User

    @ManyToOne
    lateinit var job: Job
    
    lateinit var yearAndMonth: LocalDate

    var score: Int = 0
    lateinit var comment: String

    override fun validate() {
        if (!validScore(score)) throw RatingException("El puntaje no puede ser mayor a 5 o menor a 1")
        if (!validDate(yearAndMonth)) throw RatingException("Fecha fuera de rango válido")
        if (!validComment(comment)) throw RatingException("Debe agregar un comentario")
        if (!jobIncludesUsers()) throw RatingException("El job a valorar no contiene a uno/ambos usuarios")
        if (userFrom.id == userTo.id) throw RatingException("Un usuario no puede calificarse a sí mismo")
    }

    private fun jobIncludesUsers(): Boolean = setOf(job.customer.id, job.professional.id).containsAll(setOf(userFrom.id, userTo.id))

    private fun validComment(comment: String): Boolean = comment.isNotBlank()

    private fun validDate(ratingDate: LocalDate): Boolean = ratingDate >= job.date

    private fun validScore(score: Int): Boolean = score in 1..5
}