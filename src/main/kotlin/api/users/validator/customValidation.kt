package api.users.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MaxCaractersValid::class])
@MustBeDocumented
annotation class MaxCharacters (
    val message: String = "stack field not valid",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)