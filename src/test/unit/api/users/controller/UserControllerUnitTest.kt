package api.users.controller

import api.users.dto.UserDTO
import api.users.entity.User
import api.users.service.UserService
import api.users.util.userDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.NestedTestConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import java.time.LocalDateTime
import java.util.*

@WebMvcTest(controllers = [UserController::class])
@AutoConfigureWebTestClient
class UserControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var userServiceMock: UserService

    @Test
    fun `shouldCreateUser`() {
        val userDTO = UserDTO(null, "Dan", "Daniel", LocalDateTime.now(), mutableSetOf("NodeJS", "ExpressJS"))

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val savedUserDTO = webTestClient
            .post()
            .uri("/v1/users")
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

    @Test
    fun `shouldValidateFieldNameIsFilled`() {
        val userDTO = UserDTO(null, "Dan", "", LocalDateTime.now(), mutableSetOf("NodeJS", "ExpressJS"))

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val response = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("userDTO.name must not be blank", response)
    }

    @Test
    fun `shouldValidateRuntimeException`() {
        val userDTO = UserDTO(
            null,
            "Dan",
            "Daniel",
            LocalDateTime.now(),
            mutableSetOf("NodeJS", "ExpressJS"))

        val errorMessage = "Unexpected Error occurred"

        every { userServiceMock.createUser(any()) } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(errorMessage, response)
    }

    @Test
    fun `shouldValidateLenghtField`() {
        val userDTO = UserDTO(
            null,
            "Dan",
            "aslkslakjslkajlskjalkjslkajlksjlakjslkajlskjlakjslkjalksjlakjlkllkaslkjllllsjsjsjssoi",
            LocalDateTime.now(),
            mutableSetOf("NodeJS", "ExpressJS"))

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val response = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("name field size exceeded", response)
    }

    @Test
    fun `shouldGetAllUsers`() {

        every { userServiceMock.getAllUsers() }.returnsMany(
            mutableListOf(
                UserDTO(id = UUID.randomUUID(),
                    nickname = "Test",
                    name = "Teste Completo",
                    birth_date = LocalDateTime.now(),
                    stack = mutableSetOf("Java", "Spring")),
                UserDTO(id = UUID.randomUUID(),
                    nickname = "test2",
                    name = "Teste Completo2",
                    birth_date = LocalDateTime.now(),
                    stack = mutableSetOf("Angular", "C"))
            )
        )

        val userDTOs = webTestClient
            .get()
            .uri("/v1/users")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(UserDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(2, userDTOs!!.size)
    }

    @Test
    fun `ShouldDeleteUser`() {

        every { userServiceMock.deleteUser(any()) } just runs

        val updatedUser = webTestClient
            .delete()
            .uri("/v1/users/{userId}", UUID.randomUUID())
            .exchange()
            .expectStatus().isNoContent
    }

}