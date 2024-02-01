package api.users.controller

import api.users.dto.UserDTO
import api.users.entity.User
import api.users.repository.UserRepository
import api.users.util.userEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class UserControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        val users = userEntityList()
        userRepository.saveAll(users)
    }

    @Test
    fun createUser() {
        val userDTO = UserDTO(null, "Dan", "David", LocalDateTime.now(), mutableSetOf("Dart", "Flutter"))

        val savedUserDTO = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(userDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedUserDTO!!.id != null
        }
    }

    @Test
    fun getAllUsers() {
        val userDTOs = webTestClient
            .get()
            .uri("/v1/users")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(UserDTO::class.java)
            .returnResult()
            .responseBody

        println("userDTOs : $userDTOs")
        Assertions.assertEquals(3, userDTOs!!.size)
    }

    @Test
    fun updateUser() {
        val user = User(null,
            nickname = "Da",
            name = "Danilo",
            birth_date = LocalDateTime.now(),
            stack = mutableSetOf("ReactJS", "Vue"))

        userRepository.save(user)

        val updatedUserDTO = UserDTO(null,
            nickname = "Da",
            name = "Danilo Mendes",
            birth_date = LocalDateTime.now(),
            stack = mutableSetOf("ReactJS", "Vue"))

        val updatedUser = webTestClient
            .put()
            .uri("/v1/users/{userId}", user.id)
            .bodyValue(updatedUserDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Danilo Mendes", updatedUser!!.name)
    }

    @Test
    fun deleteUser() {
        val user = User(null,
            nickname = "Da",
            name = "Danilo",
            birth_date = LocalDateTime.now(),
            stack = mutableSetOf("ReactJS", "Vue"))

        userRepository.save(user)

        val updatedUser = webTestClient
            .delete()
            .uri("/v1/users/{userId}", user.id)
            .exchange()
            .expectStatus().isNoContent

    }
}