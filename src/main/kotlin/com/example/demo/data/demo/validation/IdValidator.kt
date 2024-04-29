package com.example.demo.data.demo.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [IdValidator::class])
annotation class ValidId(
    val message: String = "Invalid ID",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)

class IdValidator : ConstraintValidator<ValidId, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        // Id values must not be null and in the range [0-9999]
        return value != null && value.matches(Regex("\\d{1,4}"))
    }
}