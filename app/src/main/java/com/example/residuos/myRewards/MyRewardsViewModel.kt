package com.example.residuos.myRewards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.residuos.localdata.UserRepository
import com.example.residuos.localdata.entity.RewardEntity
import kotlinx.coroutines.launch


class MyRewardsViewModel(application: Application) :
    AndroidViewModel(application) {

    private val userRepository =
        UserRepository(application.applicationContext)

    private val _rewards = MutableLiveData<List<RewardEntity>>()
    val rewards: LiveData<List<RewardEntity>> = _rewards

    fun loadMyRewards() {
        viewModelScope.launch {

            val prefs = getApplication<Application>()
                .getSharedPreferences("auth", 0)

            val username = prefs.getString("username", null)
                ?: return@launch

            val userId = userRepository.findUserIdByUsername(username)
                ?: return@launch

            val rewards = userRepository.getRewardsByUser(userId)

            _rewards.postValue(rewards)
        }
    }
}
