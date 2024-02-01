package api.users.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class MaxCaractersValid : ConstraintValidator<MaxCharacters, MutableSet<String>> {
    override fun isValid(value: MutableSet<String>?, context: ConstraintValidatorContext?): Boolean {
      return value.isNullOrEmpty() || value.all { it.length <= 32 }
    }
}