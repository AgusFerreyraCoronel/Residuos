package com.example.residuos.rankings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.residuos.rankings.RankingItem
import com.example.residuos.network.ApiService
import kotlinx.coroutines.launch

class RankingsViewModel(
    private val api: ApiService
) : ViewModel() {

    private val _ranking = MutableLiveData<List<RankingItem>>()
    val ranking: LiveData<List<RankingItem>> = _ranking

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadRanking(tipoResiduo: String? = null) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val resp = api.getRanking(tipoResiduo)
                if (resp.isSuccessful) {
                    _ranking.value = resp.body() ?: emptyList()
                } else {
                    _error.value = "Error ${resp.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _loading.value = false
            }
        }
    }
}