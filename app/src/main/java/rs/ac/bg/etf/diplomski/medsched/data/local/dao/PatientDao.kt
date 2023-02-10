package rs.ac.bg.etf.diplomski.medsched.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForPatientEntity

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
}