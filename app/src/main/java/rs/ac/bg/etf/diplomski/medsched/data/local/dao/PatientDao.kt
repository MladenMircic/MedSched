package rs.ac.bg.etf.diplomski.medsched.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForPatientEntity
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.NotificationPatientEntity

@Dao
interface PatientDao {

    @Query("SELECT * FROM AppointmentForPatientEntity")
    fun getAllAppointmentForPatientEntitiesFlow(): Flow<List<AppointmentForPatientEntity>>

    @Query("SELECT * FROM AppointmentForPatientEntity")
    suspend fun getAllAppointmentForPatientEntities(): List<AppointmentForPatientEntity>

    @Query("DELETE FROM AppointmentForPatientEntity")
    suspend fun deleteAllAppointmentForPatientEntities()

    @Insert
    suspend fun insertAppointmentForPatientEntity(appointmentForPatientEntity: AppointmentForPatientEntity)

    @Query("SELECT * FROM NotificationPatientEntity ORDER BY dateNotified DESC, timeNotified DESC")
    fun getAllNotifications(): Flow<List<NotificationPatientEntity>>

    @Insert
    suspend fun insertNotification(notificationPatientEntity: NotificationPatientEntity)

    @Update
    suspend fun updateNotification(notificationPatientEntity: NotificationPatientEntity)
}