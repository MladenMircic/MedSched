package rs.ac.bg.etf.diplomski.medsched.data.mappers

import rs.ac.bg.etf.diplomski.medsched.data.remote.dto.UserDto
import rs.ac.bg.etf.diplomski.medsched.domain.model.User
import javax.inject.Singleton

@Singleton
class UserInfoMapper {

    fun toUserDto(user: User): UserDto {
        return UserDto(
            email = user.email,
            password = user.password
        )
    }
}