package com.example.residuos.localdata

import android.content.Context
import com.example.residuos.localdata.database.AppDatabase
import com.example.residuos.localdata.entity.User
import android.util.Log


class UserRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val userDao = db.userDao()

    suspend fun insertUser(username: String) {
        userDao.insert(
            User(
                username = username,
                puntos = 0
            )
        )
    }
    suspend fun spendPoints(userId: Int, amount: Int): Boolean {
        val user = userDao.getUserById(userId)

        if (user == null) {
            Log.d("REDEEM_DEBUG", "Usuario no encontrado en BD")
            return false
        }

        Log.d("REDEEM_DEBUG", "Puntos actuales en BD: ${user.puntos}")
        Log.d("REDEEM_DEBUG", "Intentando gastar: $amount")
        Log.d("REDEEM_DEBUG", "User encontrado: ${user?.username} - puntos: ${user?.puntos}")
        val nuevos = user.puntos - amount
        userDao.updatePoints(userId, nuevos)
        return true

    }

    suspend fun findUserIdByUsername(username: String): Int? {
        return userDao.findByUsername(username)?.id
    }

    suspend fun getSpentPoints(username: String): Int {
        return userDao.findByUsername(username)?.puntos ?: 0
    }


}