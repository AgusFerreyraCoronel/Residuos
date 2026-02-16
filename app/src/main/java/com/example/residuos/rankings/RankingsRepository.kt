package com.example.residuos.rankings

import android.content.Context
import com.example.residuos.network.RetrofitClient

class RankingsRepository(context: Context) {

    private val api = RetrofitClient.getApiService(context)

    suspend fun getGlobalRanking(): Result<List<RankingItem>> {
        return try {
            val response = api.getRanking()

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}