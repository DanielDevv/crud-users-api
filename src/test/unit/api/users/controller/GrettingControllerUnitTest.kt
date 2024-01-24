package api.users.controller

import api.users.service.GrettingService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [GrettingController::class])
@AutoConfigureWebTestClient
class GrettingControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var grettingServiceMock: GrettingService

    @Test
    fun  retrieveGreeting() {

        val name = "daniel"

        every { grettingServiceMock.retrieveGretting(any()) } returns "$name, Hello from default profile"

        val result = webTestClient.get()
            .uri("/v1/grettings/{name}", name)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(String::class.java)
            .returnResult()

        Assertions.assertEquals("$name, Hello from default profile", result.responseBody)
    }
}