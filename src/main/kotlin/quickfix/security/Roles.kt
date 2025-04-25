package quickfix.security

enum class Roles(val roleName: String) {
    ADMIN("ADMIN"),
    READONLY("READONLY"),
    CUSTOMER("CUSTOMER"),
    PROFESSIONAL("PROFESSIONAL"),
}