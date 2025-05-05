package quickfix.models

import jakarta.persistence.*
import quickfix.utils.exceptions.BusinessException

@Entity
@Table(name = "professionals")
class ProfessionalInfo : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
    @JoinColumn(name = "professional_id")
    var professionalProfessions: MutableSet<ProfessionalProfession> = mutableSetOf()

    @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var certificates: MutableSet<Certificate> = mutableSetOf()

    var balance: Double = 0.0
    var debt: Double = 0.0

    var hasVehicle: Boolean = false

    override fun validate() {}


    fun addProfession(profession: Profession) {
        this.professionalProfessions.add(
            ProfessionalProfession().apply {
                this.profession = profession
            }
        )
    }

    fun hasActiveProfession(professionId: Long) =
        professionalProfessions.any { it.profession.id == professionId && it.active }

    fun disableProfession(professionId: Long) {
        val profession = professionalProfessions.find { it.profession.id == professionId }
            ?: throw BusinessException("No existe la profesión con id $professionId para deshabilitar.")
        profession.disable()
    }

    fun enableProfession(professionId: Long) {
        val profession = professionalProfessions.find { it.profession.id == professionId }
            ?: throw BusinessException("No existe la profesión con id $professionId para habilitar.")
        profession.enable()
    }

    fun hasProfessionByName(professionName: String): Boolean =
        professionalProfessions.any { it.profession.name == professionName && it.active }

    fun removeProfession(professionId: Long) {
        this.professionalProfessions.removeIf{ profession -> profession.id == professionId}
        this.deleteAllCertificates(professionId)
    }

    private fun deleteAllCertificates(professionId: Long) {
        this.certificates.removeIf { cert -> cert.profession.id == professionId }
    }

    fun validateCertificateAlreadyExists(newCertificateName: String) {
        if(certificates.any { certificate -> certificate.name == newCertificateName })
            throw BusinessException("Ya tiene un certificado con el mismo nombre.")
    }

    fun addCertificate(newCertificate: Certificate) {
        this.certificates.add(newCertificate)
    }

    fun deleteCertificate(stringParam: String) {
        this.certificates.removeIf { certificate -> certificate.img == stringParam || certificate.name == stringParam }
    }

    fun payDebt(amount: Double) {
        if (amount <= 0.0) throw BusinessException("El monto a pagar debe ser mayor a cero.")
        if(debt < 1) throw BusinessException("No tiene deudas pendientes")
        if (debt < amount) throw BusinessException("No tiene suficiente plata para pagar.")
        debt -= debt
    }

    fun validateCanBid(maxAllowedDebt: Double) {
        if (debt > maxAllowedDebt) {
            throw BusinessException(
                "No puede ofertar: su deuda (${debt}) supera el máximo permitido ($maxAllowedDebt)."
            )
        }
    }

}