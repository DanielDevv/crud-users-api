package api.users.dto

import api.users.validator.MaxCharacters
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.util.*

data class UserDTO(
    val id: UUID?,

    @get:Size(max = 32, message = "nickname field size exceeded")
    val nickname: String?,

    @get:NotBlank(message = "userDTO.name must not be blank")
    val name: String,

    val birthDate: LocalDateTime,

    @MaxCharacters
    val stack: Set<StackDTO>
)