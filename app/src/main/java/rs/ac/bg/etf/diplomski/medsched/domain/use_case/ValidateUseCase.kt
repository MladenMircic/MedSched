package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import android.util.Patterns
import rs.ac.bg.etf.diplomski.medsched.R

open class ValidateUseCase {

    open fun validate(text: String): ValidationResult {
        if (text.isBlank()) {
            return ValidationResult(
                success = false,
                errorId = R.string.empty_field
            )
        }
        return ValidationResult(success = true)
    }
}

object EmailValidation: ValidateUseCase() {

    override fun validate(text: String): ValidationResult {
        val isBlankValidation = super.validate(text)
        if (!isBlankValidation.success) return isBlankValidation

        if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            return ValidationResult(
                success = false,
                errorId = R.string.email_error_format
            )
        }
        return ValidationResult(success = true)
    }
}

object PasswordValidation: ValidateUseCase() {

    override fun validate(text: String): ValidationResult {
        val isBlankValidation = super.validate(text)
        if (!isBlankValidation.success) return isBlankValidation

        if (text.length < 8 ||
            !(text.any { it.isDigit() } && text.any { it.isUpperCase() })
        ) {
            return ValidationResult(
                success = false,
                errorId = R.string.password_error_register
            )
        }
        return ValidationResult(success = true)
    }
}

object ConfirmPasswordEqualsValidation: ValidateUseCase() {

    fun validate(text: String, compareText: String): ValidationResult {
        val isBlankValidation = super.validate(text)
        if (!isBlankValidation.success) return isBlankValidation

        if (text != compareText) {
            return ValidationResult(
                success = false,
                errorId = R.string.confirm_password_error
            )
        }
        return ValidationResult(success = true)
    }
}

object PhoneValidation: ValidateUseCase() {

    override fun validate(text: String): ValidationResult {
        val isBlankValidation = super.validate(text)
        if (!isBlankValidation.success) return isBlankValidation

        if (!Patterns.PHONE.matcher(text).matches()) {
            return ValidationResult(
                success = false,
                errorId = R.string.phone_error
            )
        }
        return ValidationResult(success = true)
    }
}

object SSNValidation: ValidateUseCase() {

    override fun validate(text: String): ValidationResult {
        val isBlankValidation = super.validate(text)
        if (!isBlankValidation.success) return isBlankValidation

        if (!text.all { it.isDigit() } || text.length < 9) {
            return ValidationResult(
                success = false,
                errorId = R.string.ssn_error
            )
        }
        return ValidationResult(success = true)
    }
}