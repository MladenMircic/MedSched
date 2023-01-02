package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class SearchDoctorsUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke() {

    }
}