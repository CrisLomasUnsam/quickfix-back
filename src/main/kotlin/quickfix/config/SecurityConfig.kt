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
import quickfix.security.Roles

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
                    .allowedMethods("POST", "GET","PATCH", "DELETE")
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
                    "/registration",
                    "registration/confirm",
                    "/login"
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
                    "/registration",
                    "/registration/confirm",
                    "/login",
                    ).permitAll()
                it.requestMatchers(HttpMethod.OPTIONS).permitAll()
                it.requestMatchers(HttpMethod.POST, "/user/**","/professional/**","/rating/**").hasAnyAuthority(Roles.ADMIN.name,Roles.CUSTOMER.name,Roles.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.PATCH, "/user/**","/rating/**").hasAnyAuthority(Roles.ADMIN.name,Roles.CUSTOMER.name,Roles.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.DELETE, "/user/**","/professional/**").hasAnyAuthority(Roles.ADMIN.name,Roles.CUSTOMER.name,Roles.PROFESSIONAL.name)
                it.anyRequest().authenticated()
                }
            .httpBasic(
                Customizer.withDefaults())
            .sessionManagement {
                configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(
                jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling(
                Customizer.withDefaults())
            .build()
    }
}