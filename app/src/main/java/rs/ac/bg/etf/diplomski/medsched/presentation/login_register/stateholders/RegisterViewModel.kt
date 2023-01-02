package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.*
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.events.RegisterEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states.RegisterState
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel()  {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    private fun setFieldValue(registerField: RegisterField, text: String) {
        when (registerField) {
            RegisterField.EMAIL ->
                _registerState.update { it.copy(email = text) }
            RegisterField.FIRST_NAME ->
                _registerState.update { it.copy(firstName = text) }
            RegisterField.LAST_NAME ->
                _registerState.update { it.copy(lastName = text) }
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

    fun onEvent(registerEvent: RegisterEvent) {
        when (registerEvent) {
            is RegisterEvent.FieldChange -> {
                setFieldValue(registerEvent.registerField, registerEvent.value)
            }
            is RegisterEvent.Submit -> {
                if (validateRegisterForm()) {
                    registerUser()
                }
            }
        }
    }

    private fun validateRegisterForm(): Boolean {
        val stateVal = _registerState.value
        val emailResult = EmailValidation.validate(stateVal.email)
        val firstNameResult = FormValidation().validate(stateVal.firstName)
        val lastNameResult = FormValidation().validate(stateVal.lastName)
        val passwordResult = PasswordValidation.validate(stateVal.password)
        val confirmPasswordResult = ConfirmPasswordEqualsValidation.validate(
            stateVal.confirmPassword, stateVal.password
        )
        val phoneResult = PhoneValidation.validate(stateVal.phone)
        val ssnResult = LBOValidation.validate(stateVal.ssn)

        val hasError = listOf(
            emailResult,
            firstNameResult,
            lastNameResult,
            passwordResult,
            confirmPasswordResult,
            phoneResult,
            ssnResult
        ).any { it.errorId != null }

        _registerState.update {
            it.copy(
                emailError = emailResult.errorId,
                firstNameError = firstNameResult.errorId,
                lastNameError = lastNameResult.errorId,
                passwordError = passwordResult.errorId,
                confirmPasswordError = confirmPasswordResult.errorId,
                phoneError = phoneResult.errorId,
                ssnError = ssnResult.errorId
            )
        }

        return !hasError
    }

    private fun registerUser() = viewModelScope.launch {
        val response = registerUseCase(
            Patient(
                email = _registerState.value.email,
                firstName = _registerState.value.firstName,
                lastName = _registerState.value.lastName,
                password = _registerState.value.password,
                role = 1, // Patient
                phone = _registerState.value.phone,
                ssn = _registerState.value.ssn
            )
        )

        response.collect {
            _registerState.update { value -> value.copy(isLoading = false) }
            when (it) {
                is Resource.Success -> {
                    _registerState.update { value -> value.copy(isSuccess = true) }
                    _registerState.update { value -> value.copy(snackBarMessage = R.string.registration_success) }
                }
                is Resource.Error -> {
                    _registerState.update { value -> value.copy(snackBarMessage = it.message) }
                }
                is Resource.Loading -> {
                    _registerState.update { value -> value.copy(isLoading = true) }
                }
            }
        }

    }

    enum class RegisterField {
        EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, CONFIRM_PASSWORD, PHONE, LBO
    }
}