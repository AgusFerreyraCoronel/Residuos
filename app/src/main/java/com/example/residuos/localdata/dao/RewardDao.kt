package com.example.residuos.localdata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.residuos.localdata.entity.RewardEntity

@Dao
interface RewardDao {

    @Insert
    suspend fun insertReward(reward: RewardEntity)

    @Query("SELECT * FROM rewards WHERE id_usuario = :userId")
    suspend fun getRewardsByUser(userId: Int): List<RewardEntity>
}
