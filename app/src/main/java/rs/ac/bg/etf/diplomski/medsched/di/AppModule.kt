package rs.ac.bg.etf.diplomski.medsched.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import rs.ac.bg.etf.diplomski.medsched.data.remote.LoginRegisterApi
import rs.ac.bg.etf.diplomski.medsched.commons.Constants
import rs.ac.bg.etf.diplomski.medsched.data.mappers.UserInfoMapper
import rs.ac.bg.etf.diplomski.medsched.data.repository.LoginRegisterRepositoryImpl
import rs.ac.bg.etf.diplomski.medsched.domain.repository.LoginRegisterRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesLoginRegisterApi(): LoginRegisterApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(LoginRegisterApi::class.java)

    @Singleton
    @Provides
    fun providesUserInfoMapper(): UserInfoMapper = UserInfoMapper()

    @Singleton
    @Provides
    fun providesLoginRegisterRepository(
        loginRegisterApi: LoginRegisterApi,
        userInfoMapper: UserInfoMapper
    ): LoginRegisterRepository =
        LoginRegisterRepositoryImpl(loginRegisterApi, userInfoMapper)
}