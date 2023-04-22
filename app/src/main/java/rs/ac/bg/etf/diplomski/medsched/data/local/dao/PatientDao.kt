package rs.ac.bg.etf.diplomski.medsched.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForPatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentServicePatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.NotificationPatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.relations.AppointmentWithServicesPatient

@Dao
interface PatientDao {

    @Transaction
    @Query("SELECT * FROM AppointmentForPatientEntity")
    fun getAllAppointmentForPatientEntitiesFlow(): Flow<List<AppointmentWithServicesPatient>>

    @Transaction
    @Query("SELECT * FROM AppointmentForPatientEntity")
    suspend fun getAllAppointmentForPatientEntities(): List<AppointmentWithServicesPatient>

    @Query("DELETE FROM AppointmentForPatientEntity")
    suspend fun deleteAllAppointmentForPatientEntities()

    @Query("DELETE FROM AppointmentServicePatientEntity")
    suspend fun deleteAllServicesForPatientAppointments()

    @Query("UPDATE AppointmentForPatientEntity " +
            "SET confirmed = false " +
            "WHERE appointment_id = :appointmentId"
    )
    suspend fun markAppointmentCancelled(appointmentId: Int)

    @Query("DELETE FROM AppointmentServicePatientEntity WHERE appointment_id = :appointmentId")
    suspend fun deleteAppointmentServicePatientEntity(appointmentId: Int)

    @Insert
    suspend fun insertAppointmentForPatientEntity(
        appointmentForPatientEntity: AppointmentForPatientEntity
    ): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServicesForAppointment(
        vararg appointmentServicePatientEntities: AppointmentServicePatientEntity
    )

    @Query("SELECT * FROM NotificationPatientEntity ORDER BY dateNotified DESC, timeNotified DESC")
    fun getAllNotifications(): Flow<List<NotificationPatientEntity>>

    @Insert
    suspend fun insertNotification(notificationPatientEntity: NotificationPatientEntity)

    @Update
    suspend fun updateNotification(notificationPatientEntity: NotificationPatientEntity)
}