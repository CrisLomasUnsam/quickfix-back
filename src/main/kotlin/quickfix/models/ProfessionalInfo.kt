package quickfix.models

import jakarta.persistence.*
import quickfix.utils.exceptions.BusinessException

@Entity
@Table(name = "professionals")
class ProfessionalInfo : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var professionalProfessions: MutableSet<ProfessionalProfession> = mutableSetOf()

    @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
    var certificates: MutableSet<Certificate> = mutableSetOf()

    var balance: Double = 0.0
    var debt: Double = 0.0

    var hasVehicle: Boolean = false
    override fun validate() {}

    fun addProfession(profession: Profession, active: Boolean = true) {
        this.professionalProfessions.add(
            ProfessionalProfession().apply {
                this.professionalInfo = this@ProfessionalInfo
                this.profession = profession
                this.active = active
            }
        )
    }

    fun hasActiveProfession(professionId: Long) =
        professionalProfessions.any { it.profession.id == professionId && it.active }

    fun disableProfession(professionId: Long) {
        val relation = professionalProfessions.find { it.profession.id == professionId }
            ?: throw BusinessException("No existe la profesión con id $professionId para deshabilitar.")
    }

    fun enableProfession(professionId: Long) {
        val relation = professionalProfessions.find { it.profession.id == professionId }
            ?: throw BusinessException("No existe la profesión con id $professionId para habilitar.")
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

    fun deleteCertificate(certificateName: String) {
        this.certificates.removeIf { certificate -> certificate.name == certificateName }
    }

//    private fun addConfirmedJob(): Boolean = TODO("Implement me")
//    private fun removeConfirmedJob(): Boolean = TODO("Implement me")

}