package rs.ac.bg.etf.diplomski.medsched.data.mappers

import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.LoginRequestDto
import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.request.RegisterRequestDto
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Doctor
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.Patient
import rs.ac.bg.etf.diplomski.medsched.domain.model.business.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoMapper @Inject constructor() {

    fun toLoginRequestDto(user: User): LoginRequestDto {
        return LoginRequestDto(
            email = user.email,
            password = user.password,
            role = user.role
        )
    }

    fun toRegisterRequestDto(user: User): RegisterRequestDto {
        when (user) {
            is Patient -> {
                return RegisterRequestDto(
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    password = user.password,
                    role = user.role,
                    phone = user.phone,
                    ssn = user.ssn
                )
            }
            is Doctor -> {
                return RegisterRequestDto(
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    password = user.password,
                    role = user.role,
                    phone = user.phone
                )
            }
            else -> {
                return RegisterRequestDto()
            }
        }
    }
}