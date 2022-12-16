package rs.ac.bg.etf.diplomski.medsched.login_register_module.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.data.remote.entities.UserEntity
import rs.ac.bg.etf.diplomski.medsched.repository.LoginRepository
import rs.ac.bg.etf.diplomski.medsched.utils.LoginUIState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginUIState())
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

    fun loginUser() = viewModelScope.launch {
        val response = loginRepository.loginUser(
            UserEntity(
                email = _loginState.value.email,
                password = _loginState.value.password
            )
        )
        if (response.data != null) {
            _loginStatusChannel.send(response.data.message)
        }
        else {
            _loginStatusChannel.send(response.message ?: "NOTHING")
        }

    }
}