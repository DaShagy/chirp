package com.jjasystems.chirp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform