// PointsViewModel.kt
package com.example.residuos.points

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.residuos.network.RetrofitClient
import kotlinx.coroutines.launch

class PointsViewModel(application: Application) : AndroidViewModel(application) {

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
                    _points.postValue(body?.get("puntos") ?: 0)
                } else {
                    _error.postValue("Error al obtener puntos")
                }
            } catch (e: Exception) {
                _error.postValue("Error de conexión")
            }
        }
    }
}
