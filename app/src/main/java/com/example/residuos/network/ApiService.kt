package com.example.residuos.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/api/login/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/signup/")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("/api/logout/")
    suspend fun logout(@Body body: Map<String, String>): Response<Unit>

    @POST("/api/token/refresh/")
    suspend fun refreshToken(@Body body: Map<String, String>): Response<LoginResponse>

    // Endpoints residuo (ejemplos)
    @POST("/api/residuos/reclamar/")
    suspend fun reclamar(@Body body: Map<String, String>): Response<Unit>

    @GET("/api/residuos/{id_usuario}")
    suspend fun getResiduosUsuario(@Path("id_usuario") id: String): Response<Map<String, Int>>

    @GET("/api/residuos/")
    suspend fun getResiduosTotales(): Response<Map<String, Int>>
}