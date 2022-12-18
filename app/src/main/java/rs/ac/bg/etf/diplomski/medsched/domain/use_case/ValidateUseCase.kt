package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import android.util.Patterns
import rs.ac.bg.etf.diplomski.medsched.R

open class FormValidation {
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

object EmailValidation: FormValidation() {

    override fun validate(text: String): ValidationResult {
        val isBlankValidation = super.validate(text)
        if (!isBlankValidation.success) return isBlankValidation

        if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            return ValidationResult(
                success = false,
                errorId = R.string.email
            )
        }
        return ValidationResult(success = true)
    }
}

object PasswordValidation: FormValidation() {

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

object ConfirmPasswordEqualsValidation: FormValidation() {

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

object PhoneValidation: FormValidation() {

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

object LBOValidation: FormValidation() {

    override fun validate(text: String): ValidationResult {
        val isBlankValidation = super.validate(text)
        if (!isBlankValidation.success) return isBlankValidation

        if (!text.all { it.isDigit() } && text.length != 11) {
            return ValidationResult(
                success = false,
                errorId = R.string.ssn_error
            )
        }
        return ValidationResult(success = true)
    }
}