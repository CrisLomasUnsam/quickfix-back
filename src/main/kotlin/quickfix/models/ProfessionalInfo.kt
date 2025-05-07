package quickfix.models

import jakarta.persistence.*
import quickfix.utils.MAXIMUM_DEBT
import quickfix.utils.exceptions.BusinessException


@Entity
@Table(name = "professionals")
class ProfessionalInfo : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @ManyToMany(cascade = [(CascadeType.ALL)])
    var professions: MutableSet<Profession> = mutableSetOf()

    @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var certificates: MutableSet<Certificate> = mutableSetOf()

    var balance: Double = 0.0
    var debt: Double = 0.0

    var hasVehicle: Boolean = false
    override fun validate() {
        if (balance < 0) throw BusinessException("El saldo (balance) no puede ser negativo.")
        if (debt < 0) throw BusinessException("La deuda (debt) no puede ser negativa.")
        certificates.forEach { cert ->
            if (!hasProfession(cert.profession.id)) {
                throw BusinessException("El certificado '${cert.name}' pertenece a una profesión no asignada.")
            }
        }
    }

    fun addProfession(profession: Profession) {
        this.professions.add(profession)
    }

    fun hasProfession(professionId: Long): Boolean =
        this.professions.any { it.id == professionId }

    fun hasProfessionByName(professionName: String): Boolean =
        this.professions.any { it.name == professionName }

    fun removeProfession(professionId: Long) {
        this.professions.removeIf{ it.id == professionId }
        this.deleteAllCertificates(professionId)
    }

    private fun deleteAllCertificates(professionId: Long) {
        this.certificates.removeIf { it.profession.id == professionId }
    }

    fun validateCertificateAlreadyExists(newCertificateName: String) {
        if(certificates.any { it.name == newCertificateName })
            throw BusinessException("Ya tiene un certificado con el mismo nombre.")
    }

    fun addCertificate(newCertificate: Certificate) {
        if (!hasProfession(newCertificate.profession.id)) {
            throw BusinessException("No se puede agregar un certificado para una profesión no asignada.")
        }
        validateCertificateAlreadyExists(newCertificate.name)
        this.certificates.add(newCertificate)
    }

    fun deleteCertificate(certificateName: String) {
        val cert= certificates.find { it.name == certificateName }
            ?: throw  BusinessException("no existe un certificado con ese nombre.")
        if (!hasProfession(cert.profession.id)) {
            throw BusinessException("No se puede eliminar un certificado de una profesión no asignada.")
        }
        this.certificates.removeIf { it.name == certificateName }
    }

    fun canOfferJob(){
        if (this.debt >= MAXIMUM_DEBT) {
            throw BusinessException("No puede ofertar trabajos con una deuda igual o superior a $MAXIMUM_DEBT.")
        }
    }

    fun payDebt(){
        if (balance < debt) {
            throw BusinessException("No es posible pagar la deuda de $debt con el saldo disponible de $balance.")
        }
        balance -= debt
        debt = 0.0
    }
}
