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
import quickfix.dao.TokenRepository
import quickfix.dao.UserRepository
import quickfix.dto.register.NewCredentialRequestDTO

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

    @BeforeAll
    fun init() {
        userRepository.save(CustomerBuilder.buildMock("Valentino"))
    }

    @AfterAll
    fun final() {
        userRepository.deleteAll()
        tokenRepository.deleteAll()
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
}