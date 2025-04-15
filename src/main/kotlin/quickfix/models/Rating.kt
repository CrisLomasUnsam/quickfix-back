package quickfix.models

import jakarta.persistence.*
import quickfix.utils.exceptions.BusinessException
import java.time.YearMonth

@Entity
class Rating : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @OneToOne(cascade = [(CascadeType.ALL)])
    lateinit var userFrom: User

    @OneToOne(cascade = [(CascadeType.ALL)])
    lateinit var userTo: User

    @OneToOne(cascade = [(CascadeType.ALL)])
    lateinit var job: Job
    
    var score: Int = 0
    lateinit var yearAndMonth: YearMonth
    lateinit var comment: String

    override fun validate() {
        if (!validScore(score)) throw BusinessException("El puntaje no puede ser mayor a 5 o menor a 1")
        if (!validaDate(yearAndMonth)) throw BusinessException("Fecha fuera de rango válido")
        if (!validComment(comment)) throw BusinessException("Debe agregar un comentario")
    }

    private fun validComment(comment: String): Boolean = comment.isNotBlank()

    private fun validaDate(date: YearMonth): Boolean = date.isBefore(YearMonth.now()) || date == YearMonth.now()

    private fun validScore(score: Int): Boolean = score in 1..5
}
