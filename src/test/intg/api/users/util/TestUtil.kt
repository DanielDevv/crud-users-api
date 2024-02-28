import api.users.dto.StackDTO
import api.users.entity.Stack
import java.time.LocalDateTime

import api.users.dto.UserDTO
import api.users.entity.User
import java.util.*

fun userEntityList() = listOf(
    User(null,
        nickname = "Da",
        name = "Davi",
        birthDate = LocalDateTime.now(),
        stack = setOf(Stack(null, "React", 30, null))
    ),
    User(null,
        nickname = "test",
        name = "Teste Completo",
        birthDate = LocalDateTime.now(),
        stack = setOf(Stack(null, "Kotlin", 40, null))
    ),
    User(null,
        nickname = "test2",
        name = "Teste Completo2",
        birthDate = LocalDateTime.now(),
        stack = setOf(Stack(null, "Java", 2, null))
    )
)

fun userDTO(
    id: UUID? = null,
    nickname: String = "Biel",
    name: String = "Gabriel",
    birth_date: LocalDateTime = LocalDateTime.now(),
    stack: Set<StackDTO> = setOf(StackDTO("Vue", 15))
) = UserDTO(
    id,
    nickname,
    name,
    birth_date,
    stack
)

fun stackList(): List<StackDTO> {
    return listOf(
        StackDTO("Roboto", 25),
        StackDTO("Cucumber", 50),
        StackDTO("OpenTelemetry", 70),
    )
}

data class errorFormat(
    val code: String,
    val description: String
)

data class ResponseErrorUtilList(
    val error_message: MutableList<errorFormat>
)

data class ResponseErrorUtil(
    val error_message: errorFormat
)