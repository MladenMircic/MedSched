package rs.ac.bg.etf.diplomski.medsched.presentation.login_register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.User
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.EmailValidation
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.LoginUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.PasswordValidation
import rs.ac.bg.etf.diplomski.medsched.presentation.login_register.states.LoginState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _loginStatusChannel = Channel<String>()
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

    fun resetLoginState() {
        _loginState.update {
            it.copy(
                currentSelectedRole = null,
                email = "",
                emailError = null,
                password = "",
                passwordError = null
            )
        }
    }

    fun validateLoginForm(): Boolean {
        val emailResult = EmailValidation.validate(_loginState.value.email)
        val passwordResult = PasswordValidation.validate(_loginState.value.password)
        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { it.errorMessage != null }

        if (hasError) {
            _loginState.update {
                it.copy(
                    emailError = emailResult.errorMessage,
                    passwordError = passwordResult.errorMessage
                )
            }
        }

        return !hasError
    }

    fun loginUser() = viewModelScope.launch {
        val response = loginUseCase(
            User(
                email = _loginState.value.email,
                password = _loginState.value.password
            )
        )
        when (response) {
            is Resource.Success -> {
                _loginState.update {
                    it.copy(
                        emailError = response.data?.emailError,
                        passwordError = response.data?.passwordError
                    )
                }
            }
            is Resource.Error -> {
                response.message?.let { _loginStatusChannel.send(it) }
            }
            is Resource.Loading -> {

            }
        }
    }
}