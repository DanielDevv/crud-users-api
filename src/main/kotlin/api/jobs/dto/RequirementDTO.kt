package api.jobs.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class levelStructure(
    @get:Max(value = 100, message = "min field size exceeded")
    @get:Min(value = 1, message = "min field size cannot be zero")
    val min: Int,

    @get:Max(value = 100, message = "max field size exceeded")
    @get:Min(value = 1, message = "max field size cannot be zero")
    val max: Int
)

data class RequirementDTO(
    @get:NotBlank(message = "RequirementDTO.name must not be blank")
    @get:Length(max = 32, message = "stack field size exceeded")
    val stack: String,

    @get:Valid
    val level: levelStructure
)