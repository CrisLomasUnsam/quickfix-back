package quickfix.models

import jakarta.persistence.*
import quickfix.utils.MAXIMUM_DEBT
import quickfix.utils.exceptions.ProfessionalException

@Entity
@Table(name = "professionals")
class ProfessionalInfo : Identifier {

    @Id @GeneratedValue
    override var id: Long = 0

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "professional_id", nullable = false)
    var professionalProfessions: MutableSet<ProfessionalProfession> = mutableSetOf()

    @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var certificates: MutableSet<Certificate> = mutableSetOf()

    var averageRating: Double = 0.0
    var balance: Double = 0.0
    var debt: Double = 0.0
    var hasVehicle: Boolean = false

    fun getActiveProfessions() : Set<Profession> =
        professionalProfessions.filter { it.active }.map { it.profession }.toSet()

    override fun validate() {

    if (balance < 0) throw ProfessionalException("El saldo (balance) no puede ser negativo.")
        if (debt < 0) throw ProfessionalException("La deuda (debt) no puede ser negativa.")

        certificates.forEach { cert ->
            if (!hasProfession(cert.profession.id)) {
                throw ProfessionalException("El certificado '${cert.name}' pertenece a una profesión no asignada.")
            }
        }
    }

    fun setCertificateHasImage(certificateId: Long) {
        val certificate : Certificate? = certificates.find { it.id == certificateId }
        certificate?.setHasImage()
    }

    fun addProfession(profession: Profession) {
        if (professionalProfessions.any { it.profession.id == profession.id }) {
            throw ProfessionalException("Ya existe la profesión con id ${profession.id}.")
        }
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
            ?: throw ProfessionalException("No existe la profesión con id $professionId para deshabilitar.")
        profession.disable()
    }

    fun enableProfession(professionId: Long) {
        val profession = professionalProfessions.find { it.profession.id == professionId }
            ?: throw ProfessionalException("No existe la profesión con id $professionId para habilitar.")
        profession.enable()
    }

    private fun hasProfession(professionId: Long): Boolean =
        professionalProfessions.any { it.profession.id == professionId }

    fun hasProfessionByName(professionName: String): Boolean =
        professionalProfessions.any { it.profession.name == professionName }

    fun removeProfession(professionId: Long) {
        this.professionalProfessions.removeIf{ profession -> profession.id == professionId}
        this.deleteAllCertificates(professionId)
    }

    private fun deleteAllCertificates(professionId: Long) {
        this.certificates.removeIf { it.profession.id == professionId }
    }

    fun assertCertificateExists(certificateId: Long) {
        if (certificates.none { it.id == certificateId })
            throw ProfessionalException("Ha habido un error al intentar obtener los datos del certificado.")
    }

    fun validateCertificateAlreadyExists(newCertificateName: String, profession: Profession) {
        if (certificates.any { certificate -> certificate.name.lowercase().replace(" ","") == newCertificateName.lowercase().replace(" ","") && certificate.profession == profession })
            throw ProfessionalException("Ya tiene un certificado con el mismo nombre en la profesión ${profession.name}.")
    }
    
    fun addCertificate(newCertificate: Certificate) {
        //Falta q el nombre no sea el mismo para la misma profesion
        if (certificates.any {
            it.name.equals(newCertificate.name, ignoreCase = true) }) {
            throw ProfessionalException("Ya existe un certificado con el mismo nombre.")
        }

        if (!hasProfession(newCertificate.profession.id)) {
            throw ProfessionalException("No se puede agregar un certificado para una profesión no asignada.")
        }
        validateCertificateAlreadyExists(newCertificate.name, newCertificate.profession)
        this.certificates.add(newCertificate)
    }

    fun deleteCertificate(certificateId: Long) {
        assertCertificateExists(certificateId)
        this.certificates.removeIf { it.id == certificateId }
    }

    fun payDebt() {
        if (debt <= 0)  throw ProfessionalException("No tiene deudas pendientes.")
        if (debt > balance) throw ProfessionalException("Saldo insuficiente para pagar la deuda.")
        balance -= debt
        debt = 0.0
    }

    fun validateCanOfferJob() {
        if (this.debt >= MAXIMUM_DEBT) {
            throw ProfessionalException("No puede ofertar trabajos con una deuda igual o superior a $MAXIMUM_DEBT.")
        }
    }

}
