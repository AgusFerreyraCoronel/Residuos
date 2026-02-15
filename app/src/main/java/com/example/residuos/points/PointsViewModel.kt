// PointsViewModel.kt
package com.example.residuos.points

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.residuos.localdata.UserRepository
import com.example.residuos.network.RetrofitClient
import kotlinx.coroutines.launch

class PointsViewModel(application: Application) : AndroidViewModel(application) {

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
                    val body = response.body()

                    val backendPoints = response.body()?.get("puntos") ?: 0


                    val prefs = getApplication<Application>()
                        .getSharedPreferences("auth", 0)
                    val username = prefs.getString("username", null)
                    val spentPoints = if (username != null) {
                        userRepository.getSpentPoints(username)
                    } else 0
                    //spendpoints es negativo
                    _points.postValue(backendPoints + spentPoints)
                } else {
                    _error.postValue("Error al obtener puntos")
                }
            } catch (e: Exception) {
                _error.postValue("Error de conexión")
            }
        }
    }
}
