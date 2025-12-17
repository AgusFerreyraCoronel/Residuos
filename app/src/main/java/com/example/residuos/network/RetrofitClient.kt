package com.example.residuos.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.0.101:8000/" // device -> localhost
    //private const val BASE_URL = "http://10.0.2.2:8000/" // emulator -> localhost
    private var apiService: ApiService? = null

    fun getApiService(context: Context): ApiService {
        if (apiService == null) {
            // Interceptor que añade Authorization si existe token guardado
            val authInterceptor = Interceptor { chain ->
                val shared = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val access = shared.getString("access_token", null)
                val req = if (!access.isNullOrEmpty()) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $access")
                        .build()
                } else chain.request()
                chain.proceed(req)
            }

            val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }
        return apiService!!
    }
}