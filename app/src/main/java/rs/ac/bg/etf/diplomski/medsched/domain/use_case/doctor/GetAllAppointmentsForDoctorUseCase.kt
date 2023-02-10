package rs.ac.bg.etf.diplomski.medsched.domain.use_case.doctor

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.AppointmentForDoctor
import rs.ac.bg.etf.diplomski.medsched.domain.repository.DoctorRepository
import javax.inject.Inject

class GetAllAppointmentsForDoctorUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {

    val appointmentsForDoctorFlow = doctorRepository.appointmentsForDoctorFlow

    suspend fun getAllAppointmentsForDoctorFromRemote(): List<AppointmentForDoctor> =
        doctorRepository.getAllAppointmentsForDoctorFromRemote()

    suspend fun getAllAppointmentsForDoctorFromLocal(): List<AppointmentForDoctor> =
        doctorRepository.getAllAppointmentsForDoctorFromLocal()

    suspend fun fetchAllAppointmentsForDoctorAndSaveLocal() {
        doctorRepository.fetchAllAppointmentsForDoctorAndSaveLocal()
    }
}