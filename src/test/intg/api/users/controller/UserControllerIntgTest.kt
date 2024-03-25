package api.users.controller

import ResponseErrorUtil
import ResponseErrorUtilList
import api.users.dto.Pagination
import api.users.dto.StackDTO
import api.users.dto.UserDTO
import api.users.repository.StackRepository
import api.users.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.test.context.ActiveProfiles
import java.net.URI
import java.time.LocalDateTime
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
//@Testcontainers
class UserControllerIntgTest {

    private val url = "/v1/users"

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var stackRepository: StackRepository

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        stackRepository.deleteAll()
    }

    @Nested
    inner class Create {
        @Test
        fun `Should create user with success`() {
            val userDTO = UserDTO(null, "Dan", "David", LocalDateTime.now(), setOf(StackDTO("Kotlin", 20)))

            val savedUserDTO = testRestTemplate.postForObject(url, userDTO, UserDTO::class.java)
            Assertions.assertTrue { savedUserDTO.id != null }
            Assertions.assertEquals(savedUserDTO::class, UserDTO::class)
            Assertions.assertEquals(savedUserDTO.name, userDTO.name)
            Assertions.assertEquals(savedUserDTO.nickname, userDTO.nickname)
            Assertions.assertEquals(savedUserDTO.birthDate, userDTO.birthDate)
            assertThat(savedUserDTO.stack)
                .isNotNull
                .isNotEmpty
                .isEqualTo(userDTO.stack)

            Assertions.assertEquals(savedUserDTO.stack.size, 1)
        }

        @Test
        fun `Should create user with error of validation`() {
            val userDTO = UserDTO(null, "Dan", "", LocalDateTime.now(), setOf(StackDTO("Kotlin", 101)))

            val savedUserDTO = testRestTemplate.postForObject(url, userDTO, ResponseErrorUtilList::class.java)

            println("teste erro: $savedUserDTO")

            Assertions.assertEquals(savedUserDTO.error_message.size, 2)
            Assertions.assertNotNull(savedUserDTO.error_message)
            Assertions.assertEquals(savedUserDTO.error_message.get(0).description, "stack field not valid")
            Assertions.assertEquals(savedUserDTO.error_message.get(1).description, "userDTO.name must not be blank")
        }
    }

    @Nested
   inner class AllUsers {
        @Test
        fun `Should return all users registered`() {
            val userDTOs = testRestTemplate.getForEntity(URI("${url}?page=0&size=2"), Pagination::class.java)

            println("userDTOs : $userDTOs")

            Assertions.assertEquals(2 ,userDTOs.body?.data?.page_size)
            Assertions.assertEquals(0 ,userDTOs.body?.data?.page)
        }
   }

    // teste status code

    // ordenação paginação

    @Nested
    inner class Update {
        @Test
        fun `Should update user with success`() {

            val userDTO = UserDTO(null, "Da", "Danilo", LocalDateTime.now(), setOf(StackDTO("Kotlin", 20)))

            val savedUserDTO = testRestTemplate.postForObject(url, userDTO, UserDTO::class.java)

            val updatedUserDTO = UserDTO(null,
                nickname = "Da",
                name = "Danilo Mendes",
                birthDate = LocalDateTime.now(),
                stack = setOf(StackDTO("Java", 5))
            )

            val updatedUser = testRestTemplate.exchange(
                RequestEntity<UserDTO>(
                    updatedUserDTO,
                    HttpMethod.PUT,
                    URI("${url}/${savedUserDTO.id}")
                ), UserDTO::class.java
            )

            Assertions.assertEquals("Danilo Mendes", updatedUser.body?.name)
            Assertions.assertEquals("Da", updatedUser.body?.nickname)
            Assertions.assertEquals(setOf(StackDTO("Java", 5)), updatedUser.body?.stack)
            Assertions.assertEquals(updatedUserDTO::class, UserDTO::class)
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `Should delete user with success`() {

            val userDTO = UserDTO(null, "Da", "Danilo", LocalDateTime.now(), setOf(StackDTO("Java", 5)))

            val savedUserDTO = testRestTemplate.postForObject(url, userDTO, UserDTO::class.java)

            testRestTemplate.delete(URI("${url}/${savedUserDTO.id}"))
        }
    }

    @Nested
    inner class StacksByUser {
        @Test
        fun `Should return all stacks registered by user`() {
            val userDTO = UserDTO(null, "Dan", "David", LocalDateTime.now(), setOf(StackDTO("Kotlin", 20)))

            val savedUserDTO = testRestTemplate.postForObject(url, userDTO, UserDTO::class.java)

            val getStackDTO: Set<StackDTO> = savedUserDTO.stack

            val getStack = testRestTemplate.getForObject(URI("${url}/${savedUserDTO.id}/stacks"), getStackDTO::class.java)

            assertThat(getStack)
                .isNotEmpty
                .isNotNull
                .isEqualTo(setOf(mapOf("name" to "Kotlin", "score" to 20)))
            Assertions.assertNotNull(savedUserDTO.id)
            Assertions.assertNotNull(getStack)
            Assertions.assertEquals(getStackDTO.size, userDTO.stack.size)
            assertThat(getStack)
                .isNotEmpty
                .isNotNull
        }
    }

    @Nested
    inner class GetUser {
        @Test
        fun `Should get user`() {

            val userDTO = UserDTO(null, "Da", "Danilo", LocalDateTime.of(2000, 7, 3,15,30,20), setOf(StackDTO("Java", 5)))

            val savedUserDTO = testRestTemplate.postForObject(url, userDTO, UserDTO::class.java)

            val getUser = testRestTemplate.getForObject(URI("${url}/${savedUserDTO.id}"), userDTO::class.java)
            // errorResponse
            Assertions.assertNotNull(savedUserDTO.id)
            Assertions.assertEquals(savedUserDTO.id, getUser.id)
            assertThat(getUser)
                .isNotNull
                .isEqualTo(savedUserDTO)
        }

        @Test
        fun `Should error user not found when get user`() {

            val userId = UUID.randomUUID()

            val getUser = testRestTemplate.getForObject(URI("${url}/${userId}"), ResponseErrorUtil::class.java)

            println("response getUser: $getUser")

            Assertions.assertEquals(getUser.error_message.code, "user_not_found")
            Assertions.assertEquals(getUser.error_message.description, "No user found for the passed in Id: $userId")
        }

    }
}