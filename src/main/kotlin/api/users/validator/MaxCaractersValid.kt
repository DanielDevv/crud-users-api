package api.users.validator

import api.users.dto.StackDTO
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class MaxCaractersValid : ConstraintValidator<MaxCharacters, List<StackDTO>> {
    override fun isValid(value: List<StackDTO>?, context: ConstraintValidatorContext?): Boolean {
        return value.isNullOrEmpty() || value.all { st -> !st.name.isNullOrEmpty() && st.name.length <= 32 && st.score in 1..100 }
    }
}