package com.jjasystems.chirp.core.data.mapper

import com.jjasystems.chirp.core.data.dto.AuthInfoSerializable
import com.jjasystems.chirp.core.data.dto.UserSerializable
import com.jjasystems.chirp.core.domain.auth.AuthInfo
import com.jjasystems.chirp.core.domain.auth.User

fun AuthInfoSerializable.toDomain(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toDomain()
    )
}

fun UserSerializable.toDomain(): User {
    return User(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasVerifiedEmail,
        profilePictureUrl = profilePictureUrl
    )
}