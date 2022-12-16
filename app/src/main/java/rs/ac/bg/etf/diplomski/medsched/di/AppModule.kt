package rs.ac.bg.etf.diplomski.medsched.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import rs.ac.bg.etf.diplomski.medsched.data.remote.LoginApiService
import rs.ac.bg.etf.diplomski.medsched.utils.ApiConstants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesLoginApi(): LoginApiService =
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(LoginApiService::class.java)
}