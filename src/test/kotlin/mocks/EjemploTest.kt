package mocks
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import services.EjemploController


@SpringBootTest(classes = [EjemploController::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@DisplayName("Rating controller test (use DataInitializer info)")
class DriverControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun `puedo mockear una llamada al endpoint via get y me responde correctamente`() {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/ejemplo/"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("hola"))
    }
}