package com.example.residuos.rankings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.residuos.network.RetrofitClient

class RankingsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    // Idem al Factory del Login, si agrego Hiult explota

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val api = RetrofitClient.getApiService(context)
        return RankingsViewModel(api) as T
    }
}