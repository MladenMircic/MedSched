package rs.ac.bg.etf.diplomski.medsched.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.data.remote.ClinicApi
import rs.ac.bg.etf.diplomski.medsched.data.remote.DoctorApi
import rs.ac.bg.etf.diplomski.medsched.data.remote.LoginRegisterApi
import rs.ac.bg.etf.diplomski.medsched.data.remote.PatientApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(
        dataStore: DataStore<Preferences>,
        moshi: Moshi
    ): Retrofit {
        // Logging interceptor
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        // Authorization header interceptor
        val headerInterceptor = Interceptor { chain ->
            runBlocking(Dispatchers.IO) {
                var request = chain.request()
                val pathSegments = request.url.pathSegments

                if (!pathSegments.any { Constants.SKIP_HEADER_APPEND_PATHS.contains(it) }) {
                    // TODO Promeniti logiku dohvatanja tokena
                    val token = dataStore.data.first()[PreferenceKeys.USER_TOKEN_KEY]
                    request = request.newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer $token"
                        )
                        .build()
                }

                return@runBlocking chain.proceed(request)
            }
        }

        // Building a client with all defined interceptors
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(headerInterceptor)
            addInterceptor(httpLoggingInterceptor)
        }.build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }



    @Singleton
    @Provides
    fun providesLoginRegisterApi(retrofit: Retrofit): LoginRegisterApi =
        retrofit.create(LoginRegisterApi::class.java)

    @Singleton
    @Provides
    fun providesPatientApi(retrofit: Retrofit): PatientApi =
        retrofit.create(PatientApi::class.java)

    @Singleton
    @Provides
    fun providesDoctorApi(retrofit: Retrofit): DoctorApi =
        retrofit.create(DoctorApi::class.java)

    @Singleton
    @Provides
    fun providesClinicApi(retrofit: Retrofit): ClinicApi =
        retrofit.create(ClinicApi::class.java)
}