package quickfix.models

import jakarta.persistence.*

@Entity
@Table(name = "professions")
class Profession : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @Column(unique = true, nullable = false)
    lateinit var name: String

    override fun validate() {}
}

