package com.example.residuos.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONException
import org.json.JSONObject

class ScannerViewModel : ViewModel() {

    data class ScanData(
        val id: String,
        val puntos: Int,
        val tipo: String
    )

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