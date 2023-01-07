package rs.ac.bg.etf.diplomski.medsched.presentation.login_register.stateholders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.authentication.LoginAuthUseCase
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val loginAuthUseCase: LoginAuthUseCase
): ViewModel() {

    var alreadyLogged by mutableStateOf<Boolean?>(null)

    // Authenticate the user if his token is still valid
    init {
        viewModelScope.launch {
            val response = loginAuthUseCase.authenticate()
            response.collect {
                when (it) {
                    is Resource.Success -> {
                        alreadyLogged = it.data
                    }
                    is Resource.Error -> Unit
                    is Resource.Loading -> Unit
                }
            }
        }
    }
}