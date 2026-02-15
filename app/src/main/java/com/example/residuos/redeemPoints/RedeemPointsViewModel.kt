package com.example.residuos.redeemPoints

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.residuos.localdata.UserRepository
import com.example.residuos.network.RetrofitClient
import kotlinx.coroutines.launch

class RedeemPointsViewModel(application: Application) : AndroidViewModel(application)  {

    private val userRepository: UserRepository =
        UserRepository(application.applicationContext)


    private val _points = MutableLiveData<Int>()
    val points: LiveData<Int> = _points

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadMyPoints() {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.getApiService(getApplication())
                val response = api.getPuntosPropios()

                if (response.isSuccessful) {
                    val backendPoints = response.body()?.get("puntos") ?: 0

                    val prefs = getApplication<Application>()
                        .getSharedPreferences("auth", 0)

                    val username = prefs.getString("username", null)

                    val puntosGastados = if (username != null) {
                        userRepository.getSpentPoints(username)
                    } else 0
                    //puntos gastados es negativo
                    val puntosDisponibles = backendPoints + puntosGastados

                    _points.postValue(puntosDisponibles)
                } else {
                    _error.postValue("Error al obtener puntos")
                }
            } catch (e: Exception) {
                _error.postValue("Error de conexión")
            }
        }
    }

    fun isItEnough(requiredPoints: Int): Boolean {
        val currentPoints = _points.value ?: 0
        return currentPoints >= requiredPoints
    }

    fun canjear(username: String, puntos: Int) {
        viewModelScope.launch {

            val userId = userRepository.findUserIdByUsername(username)

            if (userId == null) {
                _error.value = "Usuario no encontrado"
                return@launch
            }

            val success = userRepository.spendPoints(userId, puntos)

            if (success) {
                //_error.value = "Canje realizado"
            } else {
                _error.value = "No tenés puntos suficientes"
            }
        }
    }


}