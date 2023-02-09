package rs.ac.bg.etf.diplomski.medsched.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys.Companion.APPOINTMENT_FETCH_KEY
import rs.ac.bg.etf.diplomski.medsched.data.local.PatientDao
import rs.ac.bg.etf.diplomski.medsched.data.mappers.PatientInfoMapper
import rs.ac.bg.etf.diplomski.medsched.data.remote.PatientApi
import rs.ac.bg.etf.diplomski.medsched.di.json_adapters.toList
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
    private val patientInfoMapper: PatientInfoMapper,
    private val patientDao: PatientDao
) : PatientRepository {

    override val user: Flow<User?> = dataStore.data.map { preferences ->
        moshi.adapter(User::class.java).fromJson(
            preferences[PreferenceKeys.USER_INFO_KEY] ?: ""
        )
    }

    override val appointments: Flow<List<Appointment>?> = dataStore.data.map { preferences ->
        val appointmentJsonList = preferences[APPOINTMENT_FETCH_KEY] ?: "[]"
        moshi.toList(appointmentJsonList)
    }

    override val appointmentWithDoctorFlow: Flow<List<AppointmentWithDoctor>> =
        patientDao.getAllAppointmentEntities().map { appointmentEntityList ->
            appointmentEntityList.map { it.toAppointmentWithDoctor() }
        }

    override suspend fun fetchAndInsertIntoDbAppointmentsWithDoctor() {
        patientDao.deleteAllAppointmentEntities()
        val appointmentWithDoctorList =
            patientApi.getAllAppointmentsWithDoctor().map { it.toScheduled() }
        for (appointment in appointmentWithDoctorList) {
            patientDao.insertAppointmentEntity(appointment.toAppointmentEntity())
        }
    }

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