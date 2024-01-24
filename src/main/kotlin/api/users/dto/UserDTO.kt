package api.users.dto

import api.users.validator.MaxCharacters
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

data class UserDTO(
    val id: UUID?,

    val nickname: String?,

    @get:NotBlank(message = "userDTO.name must not be blank")
    @get:Size(max=32, message = "name field size exceeded")
    val name: String,

    val birth_date: LocalDateTime,

    @MaxCharacters
    val stack: MutableSet<String>?
)