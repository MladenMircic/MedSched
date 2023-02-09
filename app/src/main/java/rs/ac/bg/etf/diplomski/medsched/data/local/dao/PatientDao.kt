package rs.ac.bg.etf.diplomski.medsched.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentEntity

@Dao
interface PatientDao {

    @Query("SELECT * FROM AppointmentEntity")
    fun getAllAppointmentEntitiesFlow(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM AppointmentEntity")
    suspend fun getAllAppointmentEntities(): List<AppointmentEntity>

    @Query("DELETE FROM AppointmentEntity")
    suspend fun deleteAllAppointmentEntities()

    @Insert
    suspend fun insertAppointmentEntity(appointmentEntity: AppointmentEntity)
}