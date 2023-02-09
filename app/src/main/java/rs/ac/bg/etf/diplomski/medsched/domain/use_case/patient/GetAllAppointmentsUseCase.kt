package rs.ac.bg.etf.diplomski.medsched.domain.use_case.patient

import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject

class GetAllAppointmentsUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    val appointmentWithDoctorFlow = patientRepository.appointmentWithDoctorFlow

    suspend fun fetchAllAppointmentsAndInsertToDb() {
        patientRepository.fetchAndInsertIntoDbAppointmentsWithDoctor()
    }
}