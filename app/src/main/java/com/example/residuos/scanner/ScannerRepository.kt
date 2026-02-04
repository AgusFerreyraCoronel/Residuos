package com.example.residuos.scanner

import android.content.Context
import com.example.residuos.network.RetrofitClient

class ScannerRepository(context: Context) {

    private val api = RetrofitClient.getApiService(context)

    suspend fun reclamarResiduo(idResiduo: String): Result<Unit> {
        return try {
            val body = mapOf("id_residuo" to idResiduo)
            val response = api.reclamar(body)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
