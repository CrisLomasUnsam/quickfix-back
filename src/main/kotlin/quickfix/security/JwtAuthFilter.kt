package quickfix.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import quickfix.services.LoginService
import quickfix.utils.exceptions.ExpiredTokenException
import quickfix.utils.exceptions.InvalidCredentialsException

@Component
class JwtAuthFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var jwtTokenUtils : JwtTokenUtils

    @Autowired
    lateinit var loginService : LoginService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val bearerOfToken = request.getHeader("Authorization")

            if (bearerOfToken != null && bearerOfToken.startsWith("Bearer ")) {
                val token = bearerOfToken.substringAfter("Bearer ")
                val usernamePAT = jwtTokenUtils.getAuthentication(token)
                try {
                    loginService.validUser(usernamePAT.name)
                    SecurityContextHolder.getContext().authentication = usernamePAT
                    logger.info("USUARIO AUTENTICADO: $usernamePAT")

                } catch (ex: InvalidCredentialsException) {
                    logger.warn("Usuario inválido: ${usernamePAT.name}")
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
                    return
                }
            }

            try {
                filterChain.doFilter(request, response)
            } catch (ex: Exception) {
                logger.warn("OCURRIO UN ERROR EN EL FILTRO")
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
                return
            }

        } catch (e : ExpiredTokenException) {
            logger.warn(e.message)
            response.sendError(HttpStatus.UNAUTHORIZED.value(), e.message)

        }
    }
}