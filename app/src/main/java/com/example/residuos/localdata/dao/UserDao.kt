package com.example.residuos.localdata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.residuos.localdata.entity.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>
}
