package rs.ac.bg.etf.diplomski.medsched.domain.repository

import rs.ac.bg.etf.diplomski.medsched.domain.model.User
import rs.ac.bg.etf.diplomski.medsched.domain.model.UserLogin

interface LoginRegisterRepository {

    suspend fun loginUser(user: User): UserLogin
}