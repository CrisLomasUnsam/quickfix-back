package quickfix.models

import jakarta.persistence.*
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
    override fun validate() {}


    fun addProfession(profession: Profession) {
        this.professions.add(profession)
    }

    fun hasProfession(professionId: Long): Boolean =
        this.professions.any { profession -> profession.id == professionId }

    fun hasProfessionByName(professionName: String): Boolean =
        this.professions.any { profession -> profession.name == professionName }

    fun removeProfession(professionId: Long) {
        this.professions.removeIf{profession -> profession.id == professionId}
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