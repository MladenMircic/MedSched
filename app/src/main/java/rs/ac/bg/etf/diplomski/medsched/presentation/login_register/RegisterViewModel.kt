package rs.ac.bg.etf.diplomski.medsched.presentation.login_register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.*
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states.RegisterState
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel()  {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    fun setFieldValue(registerField: RegisterField, text: String) {
        when (registerField) {
            RegisterField.EMAIL ->
                _registerState.update { it.copy(email = text) }
            RegisterField.PASSWORD ->
                _registerState.update { it.copy(password = text) }
            RegisterField.CONFIRM_PASSWORD ->
                _registerState.update { it.copy(confirmPassword = text) }
            RegisterField.PHONE ->
                _registerState.update { it.copy(phone = text) }
            RegisterField.LBO ->
                _registerState.update { it.copy(ssn = text) }
        }
    }

    fun validateRegisterForm(): Boolean {
        val stateVal = _registerState.value
        val emailResult = EmailValidation.validate(stateVal.email)
        val passwordResult = PasswordValidation.validate(stateVal.password)
        val confirmPasswordResult = ConfirmPasswordEqualsValidation.validate(
            stateVal.confirmPassword, stateVal.password
        )
        val phoneResult = PhoneValidation.validate(stateVal.phone)
        val ssnResult = LBOValidation.validate(stateVal.ssn)

        val hasError = listOf(
            emailResult,
            passwordResult,
            confirmPasswordResult,
            phoneResult,
            ssnResult
        ).any { it.errorId != null }

        if (hasError) {
            _registerState.update {
                it.copy(
                    emailError = emailResult.errorId,
                    passwordError = passwordResult.errorId,
                    confirmPasswordError = confirmPasswordResult.errorId,
                    phoneError = phoneResult.errorId,
                    ssnError = ssnResult.errorId
                )
            }
        }

        return !hasError
    }

    enum class RegisterField {
        EMAIL, PASSWORD, CONFIRM_PASSWORD, PHONE, LBO
    }
}