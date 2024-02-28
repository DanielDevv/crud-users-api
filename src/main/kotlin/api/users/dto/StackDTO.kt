package api.users.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class StackDTO(
    val name: String,
    val score: Int
)