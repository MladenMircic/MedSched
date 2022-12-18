package rs.ac.bg.etf.diplomski.medsched.domain.repository

import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.LoginResponse
import rs.ac.bg.etf.diplomski.medsched.domain.model.response.RegisterResponse

interface LoginRegisterRepository {

    suspend fun loginUser(user: User): LoginResponse

    suspend fun registerUser(user: User): RegisterResponse
}