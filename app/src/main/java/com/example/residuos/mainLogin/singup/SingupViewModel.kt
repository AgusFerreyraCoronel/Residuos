package com.example.residuos.mainLogin.singup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.residuos.network.ApiService
import com.example.residuos.network.SignupRequest
import com.example.residuos.network.SignupResponse

class SignupViewModel : ViewModel() {

    private val _uiState = MutableLiveData<SignupUiState>()
    val uiState: LiveData<SignupUiState> = _uiState

    suspend fun signup(
        api: ApiService,
        username: String,
        password: String,
        email: String?
    ): SignupResponse {
        return api.signup(
            SignupRequest(
                username = username,
                password = password,
                email = email
            )
        ).body()!!
    }
}

sealed class SignupUiState {
    object Loading : SignupUiState()
    object Success : SignupUiState()
    data class Error(val message: String) : SignupUiState()
}