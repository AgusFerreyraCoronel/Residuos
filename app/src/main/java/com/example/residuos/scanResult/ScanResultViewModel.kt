package com.example.residuos.scanResult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScanResultViewModel : ViewModel() {

    private val _idResiduo = MutableLiveData<String>()
    val idResiduo: LiveData<String> = _idResiduo

    private val _puntos = MutableLiveData<Int>()
    val puntos: LiveData<Int> = _puntos

    private val _tipoResiduo = MutableLiveData<String>()
    val tipoResiduo: LiveData<String> = _tipoResiduo

    fun setData(id: String?, puntos: Int, tipo: String?) {
        _idResiduo.value = id ?: "No encontrado"
        _puntos.value = puntos
        _tipoResiduo.value = tipo ?: "No encontrado"
    }
}