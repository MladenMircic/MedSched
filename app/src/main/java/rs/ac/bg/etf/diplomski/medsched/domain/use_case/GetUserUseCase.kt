package rs.ac.bg.etf.diplomski.medsched.domain.use_case

import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    val userFlow = patientRepository.user
}