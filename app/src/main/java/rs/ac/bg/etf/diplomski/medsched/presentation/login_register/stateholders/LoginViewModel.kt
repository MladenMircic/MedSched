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
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.FormValidation
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.authentication.LoginAuthUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.events.LoginEvent
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states.LoginState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginAuthUseCase: LoginAuthUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun onEvent(loginEvent: LoginEvent) {
        when (loginEvent) {
            is LoginEvent.EmailChange -> {
                _loginState.update { it.copy(email = loginEvent.email) }
            }
            is LoginEvent.PasswordChange -> {
                _loginState.update { it.copy(password = loginEvent.password) }
            }
            is LoginEvent.RoleChange -> {
                _loginState.update { it.copy(currentSelectedRole = loginEvent.role) }
            }
            is LoginEvent.Submit -> {
                if (validateLoginForm()) {
                    loginUser()
                }
            }
        }
    }

    private fun validateLoginForm(): Boolean {
        val emailResult = FormValidation().validate(_loginState.value.email)
        val passwordResult = FormValidation().validate(_loginState.value.password)
        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { it.errorId != null }

        if (hasError) {
            _loginState.update {
                it.copy(
                    emailError = emailResult.errorId,
                    passwordError = passwordResult.errorId
                )
            }
        }

        return !hasError
    }

    private fun loginUser() = viewModelScope.launch {
        _loginState.update { it.copy(isLoading = true) }
        val response = loginAuthUseCase.login(
            Patient(
                email = _loginState.value.email,
                password = _loginState.value.password,
                role = roleMap[_loginState.value.currentSelectedRole]!!
            )
        )
        response.collect {
            _loginState.update { value -> value.copy(isLoading = false) }
            when (it) {
                is Resource.Success -> {
                    _loginState.update { value ->
                        value.copy(
                            emailError = if (it.data?.hasEmailError == true)
                                R.string.email_error_login
                            else null,
                            passwordError = if (it.data?.hasPasswordError == true)
                                R.string.password_error_login
                            else null,
                            roleError = if (it.data?.hasRoleError == true)
                                R.string.role_error_login
                            else null

                        )
                    }
                    if (it.data?.hasEmailError == false && !it.data.hasPasswordError) {
                        _loginState.update { value -> value.copy(isSuccess = true) }
                    }
                }
                is Resource.Error -> {
                    _loginState.update { value -> value.copy(snackBarMessageId = it.message) }
                }
                is Resource.Loading -> {
                    _loginState.update { value -> value.copy(isLoading = true) }
                }
            }
        }
    }

    private val roleMap = mapOf(
        "Doctor" to 0,
        "Patient" to 1
    )
}