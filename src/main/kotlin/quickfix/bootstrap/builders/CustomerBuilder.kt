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

            // Aseguramos que esté dentro del rango de DNI argentino típico
            return 10_000_000 + (hashInt % 90_000_000)
        }

        fun buildMock(userName: String): User {
            val user = User().apply {
                mail = "$userName@gmail.com"
                name = userName
                lastName = userName
                avatar = ByteArray(1)
                gender = Gender.OTHER
                address = AddressBuilder.buildMock("$userName 123")
                dateBirth = LocalDate.of(1990, 1, 1)
                verified = true
                dni = generateDniFromUserName(userName)
                setNewPassword("password")
            }
            return user
        }
    }
}