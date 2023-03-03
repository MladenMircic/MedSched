package rs.ac.bg.etf.diplomski.medsched.domain.use_case.authentication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.onesignal.OneSignal
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import okio.IOException
import retrofit2.HttpException
import rs.ac.bg.etf.diplomski.medsched.R
import rs.ac.bg.etf.diplomski.medsched.commons.PreferenceKeys
import rs.ac.bg.etf.diplomski.medsched.commons.Resource
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.LoginResponse
import rs.ac.bg.etf.diplomski.medsched.domain.repository.LoginRegisterRepository
import javax.inject.Inject

class LoginAuthUseCase @Inject constructor(
    private val loginRegisterRepository: LoginRegisterRepository,
    private val dataStore: DataStore<Preferences>,
    private val moshi: Moshi
) {
    suspend fun login(user: User): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading())
        try {
            val loginResponse = loginRegisterRepository.loginUser(user)
            // For saving JWT token to preferences data store
            loginResponse.token?.let { token ->
                dataStore.edit { preferences ->
                    preferences[PreferenceKeys.USER_TOKEN_KEY] = token
                    preferences[PreferenceKeys.USER_INFO_KEY] =
                        moshi.adapter(User::class.java).toJson(loginResponse.user)
                }
                OneSignal.setExternalUserId(loginResponse.user!!.id.toString())
            }
            emit(Resource.Success(loginResponse))
        } catch (e: HttpException) {
            emit(Resource.Error(R.string.unknown_error))
        } catch (e: IOException) {
            emit(Resource.Error(R.string.no_connection))
        }
    }

    suspend fun authenticate(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            loginRegisterRepository.authenticateUser()
            emit(Resource.Success(true))
        } catch (e: HttpException) {
            if (e.code() == HTTP_UNAUTHORIZED) {
                emit(Resource.Success(false))
            }
        } catch (e: IOException) {
            emit(Resource.Success(false))
        }
    }
}