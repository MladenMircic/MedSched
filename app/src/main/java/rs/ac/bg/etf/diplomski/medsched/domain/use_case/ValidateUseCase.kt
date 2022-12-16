package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import android.util.Patterns

interface FormValidation {
    fun validate(text: String): ValidationResult
}

object EmailValidation: FormValidation {

    override fun validate(text: String): ValidationResult {
        if (text.isBlank()) {
            return ValidationResult(
                success = false,
                errorMessage = "Email must not be empty!"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            return ValidationResult(
                success = false,
                errorMessage = "You must enter email in correct format!"
            )
        }
        return ValidationResult(success = true)
    }
}

object PasswordValidation: FormValidation {

    override fun validate(text: String): ValidationResult {
        if (text.length < 8 ||
            !(text.any { it.isDigit() } && text.any { it.isUpperCase() })
        ) {
            return ValidationResult(
                success = false,
                errorMessage =
                "Password must contain at least 8 characters, one digit and one uppercase"
            )
        }
        return ValidationResult(success = true)
    }
}