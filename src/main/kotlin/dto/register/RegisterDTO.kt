package dto.register

import models.User

data class RegisterRequestDTO(
    var email: String,
    var password: String)

fun RegisterRequestDTO.fromDTO() : User {
    return User()
}

/* data class RegisterResponseDTO() */