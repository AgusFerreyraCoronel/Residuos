package com.example.residuos.mainLogin.singup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.residuos.network.ApiService
import com.example.residuos.network.SignupRequest
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    private val _uiState = MutableLiveData<SignupUiState>()
    val uiState: LiveData<SignupUiState> = _uiState

    fun signup(
        api: ApiService,
        username: String,
        password: String,
        email: String?
    ) {
        _uiState.value = SignupUiState.Loading

        viewModelScope.launch {
            try {
                val response = api.signup(
                    SignupRequest(
                        username = username,
                        password = password,
                        email = email
                    )
                )

                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = SignupUiState.Success
                } else {
                    _uiState.value = SignupUiState.Error(
                        "Error al registrarse (${response.code()})"
                    )
                }
            } catch (e: Exception) {
                _uiState.value =
                    SignupUiState.Error("Error de red: ${e.localizedMessage}")
            }
        }
    }
}

// Clase chiquita para ver el comportamiento del singUp
sealed class SignupUiState {
    object Loading : SignupUiState()
    object Success : SignupUiState()
    data class Error(val message: String) : SignupUiState()
}