package com.octacore.demo.auth

data class LoginResponse(
    val access_token: String,
    val expires_at: Long,
    val grant_type: String,
)