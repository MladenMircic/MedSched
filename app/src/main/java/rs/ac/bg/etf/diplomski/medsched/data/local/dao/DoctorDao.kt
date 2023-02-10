package rs.ac.bg.etf.diplomski.medsched.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForDoctorEntity

@Dao
interface DoctorDao {

    @Query("SELECT * FROM AppointmentForDoctorEntity")
    fun getAllAppointmentForDoctorEntitiesFlow(): Flow<List<AppointmentForDoctorEntity>>

    @Query("SELECT * FROM AppointmentForDoctorEntity")
    suspend fun getAllAppointmentForDoctorEntities(): List<AppointmentForDoctorEntity>

    @Insert
    suspend fun insertAppointmentForDoctorEntity(
        appointmentForDoctorEntity: AppointmentForDoctorEntity
    )

    @Query("DELETE FROM AppointmentForDoctorEntity")
    suspend fun deleteAllAppointmentForDoctorEntities()
}