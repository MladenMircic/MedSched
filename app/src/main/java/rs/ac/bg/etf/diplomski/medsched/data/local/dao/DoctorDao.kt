package rs.ac.bg.etf.diplomski.medsched.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentForDoctorEntity
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.relations.AppointmentWithServicesDoctor

@Dao
interface DoctorDao {

    @Transaction
    @Query("SELECT * FROM AppointmentForDoctorEntity")
    fun getAllAppointmentForDoctorEntitiesFlow(): Flow<List<AppointmentWithServicesDoctor>>

    @Transaction
    @Query("SELECT * FROM AppointmentForDoctorEntity")
    suspend fun getAllAppointmentForDoctorEntities(): List<AppointmentWithServicesDoctor>

    @Insert
    suspend fun insertAppointmentForDoctorEntity(
        appointmentForDoctorEntity: AppointmentForDoctorEntity
    )

    @Query("DELETE FROM AppointmentForDoctorEntity")
    suspend fun deleteAllAppointmentForDoctorEntities()
}