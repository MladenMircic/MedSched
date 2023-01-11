package rs.ac.bg.etf.diplomski.medsched.presentation.patient

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

@HiltViewModel
class PatientProfileViewModel @Inject constructor(
    patientRepository: PatientRepository
): ViewModel() {

    val userFlow = patientRepository.user

}