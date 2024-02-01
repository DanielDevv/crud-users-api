package api.users.util

import api.users.dto.UserDTO
import api.users.entity.User
import java.time.LocalDateTime
import java.util.*

fun userEntityList() = mutableListOf(
    User(null,
        nickname = "Da",
        name = "Davi",
        birth_date = LocalDateTime.now(),
        stack = mutableSetOf("ReactJS", "Vue")),
    User(null,
        nickname = "test",
        name = "Teste Completo",
        birth_date = LocalDateTime.now(),
        stack = mutableSetOf("Angular", "C")),
    User(null,
        nickname = "test2",
        name = "Teste Completo2",
        birth_date = LocalDateTime.now(),
        stack = mutableSetOf("Python", "Django"))
)

fun userDTO(
    id: UUID? = null,
    nickname: String = "Biel",
    name: String = "Gabriel",
    birth_date: LocalDateTime = LocalDateTime.now(),
    stack: MutableSet<String>? = mutableSetOf("Webpack", "Babel")
) = UserDTO(
    id,
    nickname,
    name,
    birth_date,
    stack
)