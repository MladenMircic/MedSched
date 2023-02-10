package rs.ac.bg.etf.diplomski.medsched.presentation.patient.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.GetUserUseCase
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.authentication.LogoutUseCase
import javax.inject.Inject

@HiltViewModel
class PatientProfileViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    val userFlow = getUserUseCase.userFlow

    fun logout() = viewModelScope.launch {
        logoutUseCase()
    }
}