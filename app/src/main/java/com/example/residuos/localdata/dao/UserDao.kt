package com.example.residuos.localdata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.residuos.localdata.entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUserById(id: Int): User

    @Query("SELECT * FROM User WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): User?

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>

    @Query("UPDATE User SET puntos = :nuevosPuntos WHERE id = :userId")
    suspend fun updatePoints(userId: Int, nuevosPuntos: Int)

}
