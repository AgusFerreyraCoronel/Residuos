package com.example.residuos.mainLogin.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.residuos.network.RetrofitClient

class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    // El factory este es horrible pero no encontre otra forma de inicializar el elemento sin usar Hilt o alguna otra libreria
    // Es mas, cuando quise poner Hilt exploto  el proyecto y no me levantaba el gradle siquiera

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {

            val api = RetrofitClient.getApiService(context)
            val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

            return LoginViewModel(api, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
