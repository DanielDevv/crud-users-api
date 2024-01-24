package api.users.controller

import api.users.dto.UserDTO
import api.users.service.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime

@WebMvcTest(controllers = [UserController::class])
@AutoConfigureWebTestClient
class UserControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var userServiceMock: UserService

    @Test
    fun createUser() {
        val userDTO = UserDTO(null, "Dan", "Daniel", LocalDateTime.now(), mutableSetOf("NodeJS", "ExpressJS"))

        every { userServiceMock.createUser(any()) }

        val savedUserDTO = webTestClient
            .post()
            .uri("v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
           savedUserDTO!!.id != null
        }
    }
}