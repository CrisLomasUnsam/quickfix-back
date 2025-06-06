package quickfix.controllerSpec

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import quickfix.QuickFixApp
import quickfix.bootstrap.builders.CustomerBuilder
import quickfix.dao.AddressRepository
import quickfix.dao.TokenRepository
import quickfix.dao.UserRepository
import quickfix.dto.register.NewCredentialRequestDTO
import quickfix.models.Token
import java.time.LocalDateTime

@SpringBootTest(
    classes = [QuickFixApp::class],
    properties = ["spring.profiles.active=test"]
)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecoveryControllerSpec {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tokenRepository: TokenRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @BeforeAll
    fun init() {
        this.clean()
        userRepository.save(CustomerBuilder.buildMock("Valentino"))
    }

    @AfterAll
    fun clean() {
        addressRepository.deleteAll()
        tokenRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `Request change password and change it`() {

        val mail = "valentino@gmail.com"

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/recovery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mail)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))

        val token = tokenRepository.getTokensByUser(userRepository.findByMail("valentino@gmail.com").get())

        val newCredential = objectMapper.writeValueAsString(
            NewCredentialRequestDTO(
                "123456",
                token.value
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .patch("/recovery/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newCredential)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))
    }

    @Test
    fun `Try Request change password (non existing mail)`() {

        val mail = "nonexistingmail@gmail.com"

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/recovery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mail)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Usuario no encontrado nonexistingmail@gmail.com"))
    }

    @Test
    fun `Try change password (non existing token)`() {

        val newCredential = objectMapper.writeValueAsString(
            NewCredentialRequestDTO(
                "123456",
                "nonExistingToken"
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .patch("/recovery/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newCredential)
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Credenciales inválidas"))
    }

    @Test
    fun `Try change password (token expiry)`() {

        val token = Token.createTokenEntity(userRepository.findByMail("valentino@gmail.com").get())
        token.expiryDate = LocalDateTime.now().minusDays(1)
        tokenRepository.save(token)

        val newCredential = objectMapper.writeValueAsString(
            NewCredentialRequestDTO(
                "123456",
                token.value
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .patch("/recovery/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newCredential)
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Token inválido"))
    }
}