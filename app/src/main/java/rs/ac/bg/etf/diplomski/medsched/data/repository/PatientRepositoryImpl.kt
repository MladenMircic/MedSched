package rs.ac.bg.etf.diplomski.medsched.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalTime
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.data.local.dao.PatientDao
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
    private val patientInfoMapper: PatientInfoMapper,
    private val patientDao: PatientDao
) : PatientRepository {

    override val user: Flow<User?> = dataStore.data.map { preferences ->
        val currentUserString = preferences[PreferenceKeys.USER_INFO_KEY]
        moshi.adapter(User::class.java).fromJson(
            if (currentUserString == null || currentUserString == "") {
                moshi.adapter(User::class.java).toJson(EmptyUser.instance)
            } else {
                currentUserString
            }
        )
    }

    override val appointmentForPatientFlow: Flow<List<AppointmentForPatient>> =
        patientDao.getAllAppointmentForPatientEntitiesFlow().map { appointmentPatientEntityList ->
            appointmentPatientEntityList.map { it.toAppointmentForPatient() }
        }

    override suspend fun fetchAllAppointmentsForPatientAndSaveLocal() {
        patientDao.deleteAllAppointmentForPatientEntities()
        val appointmentWithDoctorList =
            patientApi.getAllAppointmentsForPatient().map { it.toAppointmentForPatient() }
        for (appointment in appointmentWithDoctorList) {
            patientDao.insertAppointmentForPatientEntity(appointment.toAppointmentForPatientEntity())
        }
    }

    override suspend fun getAppointmentsWithDoctorFromRemote(): List<AppointmentForPatient> =
        patientApi.getAllAppointmentsForPatient().map { it.toAppointmentForPatient() }

    override suspend fun getAppointmentsWithDoctorFromLocal(): List<AppointmentForPatient> =
        patientDao.getAllAppointmentForPatientEntities().map { it.toAppointmentForPatient() }

    override suspend fun getAllCategories(): List<Category> =
        patientApi.getAllCategories().map { it.toService() }

    override suspend fun getDoctors(category: String): List<DoctorForPatient> =
        patientApi.getDoctors(category).map { it.toDoctorForPatient() }

    override suspend fun getAllAppointmentsForDoctorAtDate(
        appointmentRequest: AppointmentRequest
    ): List<LocalTime> =
        patientApi.getScheduledAppointments(
            patientInfoMapper.toAppointmentRequestDto(appointmentRequest)
        )

    override suspend fun getAllServicesForDoctor(doctorId: Int): List<Service> =
        patientApi.getServicesForDoctor(doctorId).map { it.toService() }

    override suspend fun scheduleAppointment(appointment: Appointment) {
        val appointmentWithDoctor = patientApi
                .scheduleAppointment(patientInfoMapper.toAppointmentDto(appointment))
                .toAppointmentForPatient()
        patientDao.insertAppointmentForPatientEntity(appointmentWithDoctor.toAppointmentForPatientEntity())
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