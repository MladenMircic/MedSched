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
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.FormValidation
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.LoginUseCase
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states.LoginState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _loginStatusChannel = Channel<Int>()
    val loginStatusChannel = _loginStatusChannel.receiveAsFlow()

    fun setSelectedRole(roleName: String) {
        _loginState.update { it.copy(currentSelectedRole = roleName) }
    }

    fun setEmail(email: String) {
        _loginState.update { it.copy(email = email) }
    }

    fun setPassword(password: String) {
        _loginState.update { it.copy(password = password) }
    }

    fun resetState() {
        _loginState.update { LoginState() }
    }

    fun validateLoginForm(): Boolean {
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

    fun loginUser() = viewModelScope.launch {
        _loginState.update { it.copy(isLoading = true) }
        val response = loginUseCase(
            User(
                email = _loginState.value.email,
                password = _loginState.value.password
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
                            else null
                        )
                    }
                }
                is Resource.Error -> {
                    it.message?.let { message -> _loginStatusChannel.send(message) }
                }
                is Resource.Loading -> {
                    _loginState.update { value -> value.copy(isLoading = true) }
                }
            }
        }
    }
}