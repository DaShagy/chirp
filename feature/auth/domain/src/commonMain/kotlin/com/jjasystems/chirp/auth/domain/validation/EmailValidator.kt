package com.jjasystems.chirp.auth.domain.validation

object EmailValidator {
    private const val EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9._%+-]+\\.[a-zA-Z]{2,}$"

    fun validate(email: String): Boolean {
        return EMAIL_PATTERN.toRegex().matches(email)
    }
}