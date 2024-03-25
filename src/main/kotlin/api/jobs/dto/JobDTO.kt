package api.jobs.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.*

data class JobDTO(

    val id: UUID?,

    @get:Size(max = 500, message = "name field size exceeded")
    @get:NotBlank(message = "jobDTO.name must not be blank")
    val name: String,

    @get:NotBlank(message = "jobDTO.description must not be blank")
    val description: String?,

    @get:NotNull(message = "jobDTO.salary must not be null")
    val salary: Int,

    @get:Valid
    val requirements: Set<RequirementDTO> = setOf()
) {
    override fun hashCode(): Int {
        if(id != null) return id.hashCode()

        return super.hashCode()
    }
}