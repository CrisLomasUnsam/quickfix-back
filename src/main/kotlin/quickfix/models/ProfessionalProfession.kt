package quickfix.models

import jakarta.persistence.*

@Entity
@Table(name = "professional_professions")
class ProfessionalProfession{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_info_id")
    lateinit var professionalInfo: ProfessionalInfo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profession_id")
    lateinit var profession: Profession

    var active: Boolean = true

}