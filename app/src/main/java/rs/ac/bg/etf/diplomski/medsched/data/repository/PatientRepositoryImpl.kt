package rs.ac.bg.etf.diplomski.medsched.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.data.mappers.PatientInfoMapper
import rs.ac.bg.etf.diplomski.medsched.data.remote.PatientApi
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.*
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AppointmentRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.EmailChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.InfoChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.PasswordChangeResponse
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatientRepositoryImpl @Inject constructor(
    dataStore: DataStore<Preferences>,
    private val moshi: Moshi,
    private val patientApi: PatientApi,
    private val patientInfoMapper: PatientInfoMapper
) : PatientRepository {

    override val user: Flow<User?> = dataStore.data.map { preferences ->
        moshi.adapter(User::class.java).fromJson(
            preferences[PreferenceKeys.USER_INFO_KEY] ?: ""
        )
    }

    override suspend fun getAllScheduled(): List<Scheduled> =
        patientApi.getAllScheduled().map { it.toScheduled() }

    override suspend fun getAllServices(): List<Category> =
        patientApi.getAllServices().map { it.toService() }

    override suspend fun getDoctors(category: String): List<DoctorForPatient> =
        patientApi.getDoctors(category).map { it.toDoctorForPatient() }

    override suspend fun getAllAppointmentsForDoctorAtDate(
        appointmentRequest: AppointmentRequest
    ): List<Appointment> =
        patientApi.getScheduledAppointments(
            patientInfoMapper.toAppointmentRequestDto(appointmentRequest)
        ).map { it.toAppointment() }

    override suspend fun getAllServicesForDoctor(doctorId: Int): List<Service> =
        patientApi.getServicesForDoctor(doctorId).map { it.toService() }

    override suspend fun scheduleAppointment(appointment: Appointment) {
        patientApi.scheduleAppointment(patientInfoMapper.toAppointmentDto(appointment))
    }

    override suspend fun cancelAppointment(appointmentId: Int) {
        patientApi.cancelAppointment(appointmentId)
    }

    override suspend fun updateEmail(emailChangeRequest: EmailChangeRequest) {
        patientApi.updateEmail(patientInfoMapper.toEmailChangeRequestDto(emailChangeRequest))
    }

    override suspend fun updatePassword(
        passwordChangeRequest: PasswordChangeRequest
    ): PasswordChangeResponse =
        patientApi.updatePassword(
            patientInfoMapper.toPasswordChangeRequestDto(passwordChangeRequest)
        ).toPasswordChangeResponse()

    override suspend fun updateInfo(infoChangeRequest: InfoChangeRequest) {
        patientApi.updateInfo(patientInfoMapper.toInfoChangeRequestDto(infoChangeRequest))
    }

}