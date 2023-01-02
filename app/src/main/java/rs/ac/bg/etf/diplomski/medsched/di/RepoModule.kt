package rs.ac.bg.etf.diplomski.medsched.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.work.WorkManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.data.mappers.UserInfoMapper
import rs.ac.bg.etf.diplomski.medsched.data.remote.LoginRegisterApi
import rs.ac.bg.etf.diplomski.medsched.data.remote.PatientApi
import rs.ac.bg.etf.diplomski.medsched.data.repository.LoginRegisterRepositoryImpl
import rs.ac.bg.etf.diplomski.medsched.data.repository.PatientRepositoryImpl
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.*
import rs.ac.bg.etf.diplomski.medsched.domain.repository.LoginRegisterRepository
import rs.ac.bg.etf.diplomski.medsched.domain.repository.PatientRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun providesLoginRegisterRepository(
        loginRegisterApi: LoginRegisterApi,
        userInfoMapper: UserInfoMapper
    ): LoginRegisterRepository =
        LoginRegisterRepositoryImpl(loginRegisterApi, userInfoMapper)

    @Singleton
    @Provides
    fun providesPatientRepository(
        dataStore: DataStore<Preferences>,
        moshi: Moshi,
        patientApi: PatientApi
    ): PatientRepository =
        PatientRepositoryImpl(dataStore, moshi, patientApi)

    @Singleton
    @Provides
    fun providesPreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(
                SharedPreferencesMigration(context, PreferenceKeys.USER_TOKEN_PREFERENCE),
                SharedPreferencesMigration(context, PreferenceKeys.USER_INFO_PREFERENCE)
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = {
                context.preferencesDataStoreFile(PreferenceKeys.USER_TOKEN_PREFERENCE)
            }
        )

    @Singleton
    @Provides
    fun providesWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Singleton
    @Provides
    fun providesMoshiConverter(): Moshi =
        Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(User::class.java, "type")
                    .withSubtype(Patient::class.java, UserType.PATIENT.name)
                    .withSubtype(Doctor::class.java, UserType.DOCTOR.name)
            )
            .addLast(KotlinJsonAdapterFactory())
            .build()
}