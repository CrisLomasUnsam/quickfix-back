package quickfix.bootstrap.builders

import quickfix.models.Gender
import quickfix.models.User
import java.security.MessageDigest
import java.time.LocalDate

class CustomerBuilder {

    companion object {

        private fun generateDniFromUserName(userName: String): Int {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(userName.toByteArray())

            // Tomamos los primeros 4 bytes para generar un número positivo
            val hashInt = hashBytes.take(4).fold(0) { acc, byte ->
                (acc shl 8) or (byte.toInt() and 0xFF)
            }

            val dni = 10_000_000 + (hashInt % 90_000_000)
            return if(dni < 0) dni * (-1) else dni
        }

        fun buildMock(userName: String, lastName: String = "Test"): User {
            val user = User().apply {
                this.mail = "$userName@gmail.com".lowercase()
                this.name = userName
                this.lastName = lastName
                this.gender = Gender.OTHER
                this.dateBirth = LocalDate.of(1990, 1, 1)
                this.verified = true
                this.dni = generateDniFromUserName(userName)
                setNewPassword("password")
            }
            return user
        }
    }
}