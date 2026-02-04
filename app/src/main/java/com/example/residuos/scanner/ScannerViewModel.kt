package com.example.residuos.scanner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class ScannerViewModel(application: Application) : AndroidViewModel(application) {

    data class ScanData(
        val id: String,
        val puntos: Int,
        val tipo: String
    )


    private val repository = ScannerRepository(application)

    private val _navigateEvent = MutableLiveData<ScanData>()
    val navigateEvent: LiveData<ScanData> = _navigateEvent

    private val _errorEvent = MutableLiveData<String>()
    val errorEvent: LiveData<String> = _errorEvent

    private var qrHandled = false

    fun onQrScanned(rawValue: String) {
        if (qrHandled) return

        try {
            val json = JSONObject(rawValue)

            val id = json.optString("ID Residuo", "")
            val puntos = json.optInt("Puntos", -1)
            val tipo = json.optString("Tipo Residuo", "")

            if (id.isBlank() || puntos < 0 || tipo.isBlank()) {
                _errorEvent.postValue("QR inválido")
                return
            }

            qrHandled = true

            viewModelScope.launch {
                val result = repository.reclamarResiduo(id)

                result.fold(
                    onSuccess = {
                        _navigateEvent.postValue(ScanData(id, puntos, tipo))
                    },
                    onFailure = {
                        _errorEvent.postValue("No se pudo reclamar el residuo")
                        qrHandled = false
                    }
                )
            }

            _navigateEvent.postValue(ScanData(id, puntos, tipo))

        } catch (e: JSONException) {
            _errorEvent.postValue("QR inválido")
        }
    }

    override fun onCleared() {
        super.onCleared()
        qrHandled = false
    }
}