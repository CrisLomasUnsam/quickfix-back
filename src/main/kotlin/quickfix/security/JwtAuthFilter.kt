package quickfix.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import quickfix.services.UserService
import quickfix.utils.exceptions.ExpiredTokenException

@Component
class   JwtAuthFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var jwtTokenUtils : JwtTokenUtils

    @Autowired
    lateinit var userService: UserService

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
                userService.getUserById(usernamePAT.principal.toString().toLong())
                SecurityContextHolder.getContext().authentication = usernamePAT
                logger.info("usernamePostAuthToken: $usernamePAT")
            }

            filterChain.doFilter(request, response)

        } catch (e : ExpiredTokenException) {
            logger.warn(e.message)
            response.sendError(HttpStatus.valueOf(498).value(), e.message)
        }
    }
}