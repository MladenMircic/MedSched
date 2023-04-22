package rs.ac.bg.etf.diplomski.medsched.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.data.local.dao.PatientDao
import rs.ac.bg.etf.diplomski.medsched.data.mappers.PatientInfoMapper
import rs.ac.bg.etf.diplomski.medsched.data.remote.PatientApi
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.*
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.NotificationPatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.AvailableTimesRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.EmailChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.InfoChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.request.PasswordChangeRequest
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.AvailableTimesResponse
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
    override val notifications: Flow<List<NotificationPatientEntity>> =
        patientDao.getAllNotifications()

    override suspend fun fetchAllAppointmentsForPatientAndSaveLocal() {
        patientDao.deleteAllAppointmentForPatientEntities()
        patientDao.deleteAllServicesForPatientAppointments()
        val appointmentWithDoctorList =
            patientApi.getAllAppointmentsForPatient().map { it.toAppointmentForPatient() }
        for (appointmentWithDoctor in appointmentWithDoctorList) {
            patientDao.insertAppointmentForPatientEntity(appointmentWithDoctor.toAppointmentForPatientEntity())
            val appointmentServices = appointmentWithDoctor.appointment.services.map {
                it.toAppointmentServicePatientEntity(appointmentWithDoctor.appointment.id)
            }
            patientDao.insertServicesForAppointment(*appointmentServices.toTypedArray())
        }
    }

    override suspend fun getAppointmentsWithDoctorFromRemote(): List<AppointmentForPatient> =
        patientApi.getAllAppointmentsForPatient().map { it.toAppointmentForPatient() }

    override suspend fun getAppointmentsWithDoctorFromLocal(): List<AppointmentForPatient> =
        patientDao.getAllAppointmentForPatientEntities().map { it.toAppointmentForPatient() }

    override suspend fun getAllCategories(): List<Category> =
        patientApi.getAllCategories().map { it.toService() }

    override suspend fun getDoctors(doctorName: String, categories: String): List<DoctorForPatient> =
        patientApi.getDoctors(doctorName, categories).map { it.toDoctorForPatient() }

    override suspend fun getClinics(category: String): List<ClinicForPatient> =
        patientApi.getClinics(category).map { it.toClinicForPatient() }

    override suspend fun getAvailableTimesForDoctor(
        availableTimesRequest: AvailableTimesRequest
    ): AvailableTimesResponse =
        patientApi.getAvailableAppointmentTimes(
            patientInfoMapper.toAvailableTimesRequestDto(availableTimesRequest)
        ).toAvailableTimesResponse()

    override suspend fun getAllServicesForDoctor(doctorId: String): List<Service> =
        patientApi.getServicesForDoctor(doctorId).map { it.toService() }

    override suspend fun scheduleAppointments(appointments: List<Appointment>): List<Int> =
        patientApi
            .scheduleAppointments(appointments.map { patientInfoMapper.toAppointmentDto(it) })


    override suspend fun cancelAppointment(appointmentId: Int) {
        patientApi.cancelAppointment(appointmentId)
//        patientDao.deleteAppointmentForPatientEntity(appointmentId)
//        patientDao.deleteAppointmentServicePatientEntity(appointmentId)
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

    override suspend fun insertNotification(notificationPatientEntity: NotificationPatientEntity) {
        patientDao.insertNotification(notificationPatientEntity)
    }

    override suspend fun updateNotification(notificationPatientEntity: NotificationPatientEntity) {
        patientDao.updateNotification(notificationPatientEntity)
    }

}