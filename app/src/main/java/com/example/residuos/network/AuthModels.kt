package com.example.residuos.network

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val access: String, val refresh: String)

data class SignupRequest(
    val username: String,
    val password: String,
    val email: String? = null
)

data class ApiError(val detail: String?)

data class SignupResponse(
    val refresh: String,
    val access: String
)