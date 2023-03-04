package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import android.util.Log
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    patientRepository: PatientRepository
) {
    val userFlow = patientRepository.user

    fun proba() {
        Log.d("TESTIRANJE", "RADILICA")
    }
}