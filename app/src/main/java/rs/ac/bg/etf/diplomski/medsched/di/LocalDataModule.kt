package rs.ac.bg.etf.diplomski.medsched.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import rs.ac.bg.etf.diplomski.medsched.data.local.ClinicDatabase
import rs.ac.bg.etf.diplomski.medsched.data.local.PatientDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    @Singleton
    fun provideClinicDatabase(@ApplicationContext context: Context): ClinicDatabase =
        ClinicDatabase.getInstance(context)

    @Provides
    @Singleton
    fun providePatientDao(clinicDatabase: ClinicDatabase): PatientDao =
        clinicDatabase.getPatientDao()
}