package quickfix.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import quickfix.utils.exceptions.InvalidTokenException
import quickfix.utils.exceptions.InvalidCredentialsException
import java.util.*
import kotlin.time.Duration.Companion.minutes

@Component
class JwtTokenUtils {

    @Value("\${security.secret-key}")
    lateinit var secretKey: String

    @Value("\${security.access-token-minutes}")
    var accessTokenMinutes: Int = 30

    val logger: Logger = LoggerFactory.getLogger(JwtTokenUtils::class.java)

    fun createToken(id: Long, rol: String): String? {
        val longExpirationTime = accessTokenMinutes.minutes.inWholeMilliseconds

        val now = Date()

        return Jwts
            .builder()
            .subject(id.toString())
            .issuedAt(now)
            .expiration(Date(now.time + longExpirationTime))
            .claim("rol", rol)
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .compact()
    }

    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        try {
            val secret = Keys.hmacShaKeyFor(secretKey.toByteArray())
            val claims = Jwts
                .parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .payload

            if (claims.subject == null || claims.subject.isBlank()) {
                throw InvalidCredentialsException()
            }

            logger.info("Token decoded, user: " + claims.subject + " - rol: " + claims["rol"])

            val rol = SimpleGrantedAuthority(claims["rol"] as String)
            return UsernamePasswordAuthenticationToken(claims.subject, null, setOf(rol))

        } catch (expiredJwtException: ExpiredJwtException) {
            throw InvalidTokenException("Sesión vencida")
        }
    }
}