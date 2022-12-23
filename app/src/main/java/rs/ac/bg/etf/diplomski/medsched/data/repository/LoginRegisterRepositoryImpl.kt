package rs.ac.bg.etf.diplomski.medsched.data.repository

import rs.ac.bg.etf.diplomski.medsched.data.mappers.UserInfoMapper
import rs.ac.bg.etf.diplomski.medsched.data.remote.LoginRegisterApi
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.LoginResponse
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.RegisterResponse
import rs.ac.bg.etf.diplomski.medsched.domain.repository.LoginRegisterRepository
import javax.inject.Inject

class LoginRegisterRepositoryImpl @Inject constructor(
    private val loginRegisterApi: LoginRegisterApi,
    private val userInfoMapper: UserInfoMapper
) : LoginRegisterRepository {

    override suspend fun loginUser(user: User): LoginResponse =
        loginRegisterApi.loginUser(userInfoMapper.toLoginRequestDto(user)).toUserLogin()

    override suspend fun registerUser(user: User): RegisterResponse =
        loginRegisterApi.registerUser(userInfoMapper.toRegisterRequestDto(user))
            .toRegisterResponse()

    override suspend fun authenticateUser(bearerToken: String) {
        loginRegisterApi.authenticateUser("Bearer $bearerToken")
    }
}