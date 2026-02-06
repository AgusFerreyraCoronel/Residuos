package com.example.residuos.network

import com.example.residuos.rankings.RankingItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Clase general para los metodos utilziados para la comunicacion con la API del backend, podriamos ponerlos todos asi ya quedan planteados
    @POST("/api/login/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/signup/")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("/api/logout/")
    suspend fun logout(@Body body: Map<String, String>): Response<Unit>

    @POST("/api/token/refresh/")
    suspend fun refreshToken(@Body body: Map<String, String>): Response<LoginResponse>

    // Endpoints residuo (ejemplos)
    @POST("/api/residuo/reclamar/")
    suspend fun reclamar(@Body body: Map<String, String>): Response<Unit>

    @GET("/api/residuos/{id_usuario}")
    suspend fun getResiduosUsuario(@Path("id_usuario") id: String): Response<Map<String, Int>>

    @GET("/api/residuos/")
    suspend fun getResiduosTotales(): Response<Map<String, Int>>

    @GET("/api/puntos/")
    suspend fun getPuntosPropios(): Response<Map<String, Int>>

    // RANKING
    @GET("api/ranking/")
    suspend fun getRanking(
        @Query("tipo_residuo") tipoResiduo: String? = null
    ): Response<List<RankingItem>>
}