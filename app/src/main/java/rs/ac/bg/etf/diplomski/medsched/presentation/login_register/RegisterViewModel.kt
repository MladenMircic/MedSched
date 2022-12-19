package rs.ac.bg.etf.diplomski.medsched.presentation.login_register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.*
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states.RegisterState
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel()  {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val _registerFeedbackChannel = Channel<Int>()
    val registerFeedbackFlow = _registerFeedbackChannel.receiveAsFlow()

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

        _registerState.update {
            it.copy(
                emailError = emailResult.errorId,
                passwordError = passwordResult.errorId,
                confirmPasswordError = confirmPasswordResult.errorId,
                phoneError = phoneResult.errorId,
                ssnError = ssnResult.errorId
            )
        }

        return !hasError
    }

    fun registerUser() = viewModelScope.launch {
        val response = registerUseCase(
            User(
                email = _registerState.value.email,
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
                    _registerFeedbackChannel.send(R.string.registration_success)
                }
                is Resource.Error -> {
                    it.message?.let { message -> _registerFeedbackChannel.send(message) }
                }
                is Resource.Loading -> {
                    _registerState.update { value -> value.copy(isLoading = true) }
                }
            }
        }

    }

    enum class RegisterField {
        EMAIL, PASSWORD, CONFIRM_PASSWORD, PHONE, LBO
    }
}