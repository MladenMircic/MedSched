package rs.ac.bg.etf.diplomski.medsched.data.repository

import rs.ac.bg.etf.diplomski.medsched.data.mappers.UserInfoMapper
import rs.ac.bg.etf.diplomski.medsched.data.remote.LoginRegisterApi
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.toUserLogin
import rs.ac.bg.etf.diplomski.medsched.domain.model.User
import rs.ac.bg.etf.diplomski.medsched.domain.model.UserLogin
import rs.ac.bg.etf.diplomski.medsched.domain.repository.LoginRegisterRepository
import javax.inject.Inject

class LoginRegisterRepositoryImpl @Inject constructor(
    private val loginRegisterApi: LoginRegisterApi,
    private val userInfoMapper: UserInfoMapper
) : LoginRegisterRepository {

    override suspend fun loginUser(user: User): UserLogin =
        loginRegisterApi.loginUser(userInfoMapper.toUserDto(user)).toUserLogin()

}