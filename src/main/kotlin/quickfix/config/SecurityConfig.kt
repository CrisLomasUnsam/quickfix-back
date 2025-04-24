package quickfix.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import quickfix.security.JwtAuthFilter
import quickfix.utils.FRONTEND_URL
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import quickfix.security.CsrfTokenRequestHandler

@Configuration
class SecurityConfig {

    @Autowired
    lateinit var jwtAuthFilter: JwtAuthFilter

    @Bean @Throws(Exception::class)
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager = authConfig.authenticationManager

    @Bean
    fun corsConfig(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins(FRONTEND_URL)
                    .allowedHeaders("*")
                    .allowedMethods("POST", "GET", "PUT","PATCH", "DELETE")
                    .allowCredentials(true)
            }
        }
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .cors { it.disable() }
            .csrf {
                it.ignoringRequestMatchers(
                    "/quickfix-api/v1/**",
                    "/quickfix-api/swagger-config/**",
                    "/swagger",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-resources/configuration/ui",
                    "/swagger-resources/configuration/security",
                    "/webjars/**",
                    "/registration",
                    "registration/login",
                    )
                it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                it.csrfTokenRequestHandler(CsrfTokenRequestHandler())
                }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/quickfix-api/v1/**",
                    "/quickfix-api/swagger-config/**",
                    "/swagger",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-resources/configuration/ui",
                    "/swagger-resources/configuration/security",
                    "/webjars/**",
                    "/registration",
                    "/registration/login",
                    ).permitAll()
                it.requestMatchers(HttpMethod.OPTIONS).permitAll()
                    .anyRequest().authenticated()
                }
            .httpBasic(Customizer.withDefaults())
            .sessionManagement { configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling(Customizer.withDefaults())
            .build()
    }

//    @Bean
//    fun userDetailsService(userRepository: UserRepository): UserDetailsService = UserDetailsService { mail ->
//        userRepository.findByMail(mail) ?: throw UsernameNotFoundException("Usuario con email $mail no encontrado")
//    }
}

