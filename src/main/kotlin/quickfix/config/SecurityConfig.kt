package quickfix.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.SecurityFilterChain
import quickfix.dao.UserRepository

@Configuration
class SecurityConfig {
    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager = authConfig.authenticationManager

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.anyRequest().permitAll() /* Cambiará con JWT */
            //it.anyRequest().authenticated()
        }
        return http.build()
    }

    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService = UserDetailsService { mail ->
        userRepository.findByMail(mail) ?: throw UsernameNotFoundException("Usuario con email $mail no encontrado")
    }
}

