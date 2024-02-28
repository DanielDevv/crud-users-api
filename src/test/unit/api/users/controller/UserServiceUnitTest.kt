package api.users.controller

import api.users.entity.Stack
import api.users.entity.User
import api.users.exception.UserNotFoundException
import api.users.repository.StackRepository
import api.users.repository.UserRepository
import api.users.service.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import stackList
import java.time.LocalDateTime
import java.util.*

@WebMvcTest(controllers = [UserService::class])
class UserServiceUnitTest() {

    @MockkBean
    lateinit var userRepository: UserRepository

    @MockkBean
    lateinit var stackRepository: StackRepository

    @Autowired
    lateinit var userService: UserService

    @Test
    fun `Should throw UserNotFoundException when user not exists`() {
        val userId = UUID.randomUUID()

        val user = User(userId,
            nickname = "Da",
            name = "Davi",
            birthDate = LocalDateTime.now(),
            stack = setOf(Stack(null, "React", 30, null))
        )

        every { userRepository.findById(any()) } returns Optional.empty()

        val getStacks = assertThrows<UserNotFoundException> { userService.getStacks(userId) }

        Assertions.assertEquals("No user found for the passed in Id: $userId", getStacks.message)
    }
}