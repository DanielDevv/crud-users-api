package api.users.controller

//import api.users.util.userDTO
import api.users.dto.StackDTO
import api.users.dto.UserDTO
import api.users.service.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import jakarta.validation.ConstraintViolationException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.java.lazy.descriptors.DeclaredMemberIndex.Empty

@WebMvcTest(controllers = [UserController::class])
class UserControllerUnitTest {

    private val url = "/v1/users"

    @MockkBean
    lateinit var userServiceMock: UserService

    @Autowired
    lateinit var userController: UserController

    @Test
    fun `should create user with success`() {
        val userDTO = UserDTO(null, "Dan", "Daniel", LocalDateTime.now(), setOf(StackDTO("Dart", 50)))

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val savedUserDTO = userController.createUser(userDTO)

        Assertions.assertTrue {
            savedUserDTO.id != null
        }
    }

    @Test
    fun `should throw exception when field name is empty in create`() {
        val userDTO = UserDTO(null, "Dan", "", LocalDateTime.now(), setOf(StackDTO("Dart", 50)))

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val savedUserDTO = assertThrows<ConstraintViolationException> { userController.createUser(userDTO) }

        Assertions.assertNotNull(savedUserDTO)
        Assertions.assertEquals(savedUserDTO.constraintViolations.size, 1)
        Assertions.assertEquals("createUser.user.name: userDTO.name must not be blank", savedUserDTO.message)
    }


    @Test
    fun `should validate field name is filled`() {
        val userDTO = UserDTO(null, "Dan", "", LocalDateTime.now(), setOf(StackDTO("Vue", 30)))

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val response = assertThrows<ConstraintViolationException> { userController.createUser(userDTO) }

        Assertions.assertNotNull(response)
//        Assertions.assertTrue { response.constraintViolations.size == 1 } // assertEquals
        Assertions.assertEquals(response.constraintViolations.size, 1)
        Assertions.assertEquals("createUser.user.name: userDTO.name must not be blank", response.message)
    }

    @Test
    fun `should validate runtime exception`() {
        val userDTO = UserDTO(
            null,
            "Dan",
            "Daniel",
            LocalDateTime.now(),
            setOf(StackDTO("C++", 70))
        )

        val errorMessage = "Unexpected Error occurred"

        every { userServiceMock.createUser(any()) } throws RuntimeException(errorMessage)

        val response = assertThrows<RuntimeException> { userController.createUser(userDTO) }

        Assertions.assertEquals(errorMessage, response.message)
    }

    @Test
    fun `Should throw ConstraintViolationException case length nickname field greater than 32`() { // deve lançar caso ...
        val userDTO = UserDTO(
            null,
            "aslkslakjslkalkjlkjhjkhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjjk",
            "Daniel",
            LocalDateTime.now(),
            setOf(StackDTO("Vue", 30))
        )

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val response = assertThrows<ConstraintViolationException> { userController.createUser(userDTO) }

        Assertions.assertEquals("createUser.user.nickname: nickname field size exceeded", response.message)
    }

    // inner class | nested

    @Test
    fun `Should save user whet the length nickname field equal 32`() { // deve lançar caso ...
        val userDTO = UserDTO(
            null,
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            "Daniel",
            LocalDateTime.now(),
            setOf(StackDTO("Vue", 30))
        )

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val response = userController.createUser(userDTO)

        Assertions.assertNotNull(response.nickname)
        Assertions.assertEquals(response.nickname?.length, 32)
        Assertions.assertNotNull(response.id)
    }

    // teste validando a lista de stack @MaxChacarter

    @Test
    fun `should throw ConstraintViolationException when field name of stack list is empty`() {
        val userDTO = UserDTO(
            null,
            "dan",
            "Daniel",
            LocalDateTime.now(),
            setOf(StackDTO("", 30))
        )

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val response = assertThrows<ConstraintViolationException> { userController.createUser(userDTO) }

        Assertions.assertEquals("createUser.user.stack: stack field not valid", response.message)
    }

    @Test
    fun `Should throw ConstraintViolationException case length nickname field in stack list greater than 32`() { // deve lançar caso ...
        val userDTO = UserDTO(
            null,
            "dan",
            "Daniel",
            LocalDateTime.now(),
            setOf(StackDTO("asssaaajajaajaakakakaaaajsjsjsjsjjjsjsjsjsksksk", 30))
        )

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val response = assertThrows<ConstraintViolationException> { userController.createUser(userDTO) }

        Assertions.assertEquals("createUser.user.stack: stack field not valid", response.message)
    }

    @Test
    fun `Should save user whet the length nickname field in stack list equal 32`() { // deve lançar caso ...
        val userDTO = UserDTO(
            null,
            "dan",
            "Daniel",
            LocalDateTime.now(),
            setOf(StackDTO("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 30))
        )

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val response = userController.createUser(userDTO)

        Assertions.assertNotNull(response.id)
        Assertions.assertEquals(response.stack.size, 1)
//        Assertions.assertEquals(response.stack.get(0).score, 30)
        assertThat(response.stack)
            .isNotNull
            .isNotEmpty
    }

    @Test
    fun `Should throw ConstraintViolationException case score field in stack list is not between 1 until 100`() { // deve lançar caso ...
        val userDTO = UserDTO(
            null,
            "dan",
            "Daniel",
            LocalDateTime.now(),
            setOf(StackDTO("NodeJS", 101))
        )

        every { userServiceMock.createUser(any()) } returns userDTO.copy(id = UUID.randomUUID())

        val response = assertThrows<ConstraintViolationException> { userController.createUser(userDTO) }


        Assertions.assertEquals("createUser.user.stack: stack field not valid", response.message)
    }

}