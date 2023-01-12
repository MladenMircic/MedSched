package rs.ac.bg.etf.diplomski.medsched.presentation.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import rs.ac.bg.etf.diplomski.medsched.domain.use_case.authentication.LogoutUseCase
import javax.inject.Inject

@HiltViewModel
class PatientProfileViewModel @Inject constructor(
    patientRepository: PatientRepository,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    val userFlow = patientRepository.user

    fun logout() = viewModelScope.launch {
        logoutUseCase()
    }
}