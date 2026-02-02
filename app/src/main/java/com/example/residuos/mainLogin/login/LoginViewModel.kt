package com.example.residuos.mainLogin.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.residuos.network.ApiService
import com.example.residuos.network.LoginRequest
import com.example.residuos.network.LoginResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel(
    private val api: ApiService,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableLiveData<LoginUiState>()
    val uiState: LiveData<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Usuario y contraseña requeridos")
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val resp = api.login(LoginRequest(username, password))

                if (resp.isSuccessful && resp.body() != null) {
                    saveSession(username, resp.body()!!)
                    _uiState.value = LoginUiState.Success
                } else {
                    _uiState.value =
                        LoginUiState.Error("Credenciales inválidas (${resp.code()})")
                }

            } catch (e: Exception) {
                _uiState.value =
                    LoginUiState.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }

    private fun saveSession(username: String, response: LoginResponse) {
        prefs.edit()
            .putString("username", username)
            .putString("access_token", response.access)
            .putString("refresh_token", response.refresh)
            .apply()
    }
}

sealed class LoginUiState {
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}