package rs.ac.bg.etf.diplomski.medsched.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import rs.ac.bg.etf.diplomski.medsched.domain.model.entities.AppointmentEntity

@Database(entities = [AppointmentEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateTimeConverters::class)
abstract class ClinicDatabase: RoomDatabase() {
    abstract fun getPatientDao(): PatientDao

    companion object {
        private var INSTANCE: ClinicDatabase? = null

        fun getInstance(context: Context): ClinicDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ClinicDatabase::class.java,
                    "clinic_database"
                ).build()

                INSTANCE = instance
                return instance
            }
    }
}