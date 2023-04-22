package rs.ac.bg.etf.diplomski.medsched.domain.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Service

@Entity
data class AppointmentServicePatientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "appointment_id") val appointmentId: Int,
    @ColumnInfo(name = "service_id") val serviceId: Int
) {
    fun toService(): Service {
        return Service(
            id = serviceId,
            name = "",
            categoryId = -1
        )
    }
}