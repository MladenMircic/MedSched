package rs.ac.bg.etf.diplomski.medsched.login_register_module.stateholders

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import rs.ac.bg.etf.diplomski.medsched.utils.LoginUIState

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow(LoginUIState())
    val loginState = _loginState.asStateFlow()

    fun setSelectedRole(roleName: String) {
        _loginState.update { it.copy(currentSelectedRole = roleName) }
    }
}