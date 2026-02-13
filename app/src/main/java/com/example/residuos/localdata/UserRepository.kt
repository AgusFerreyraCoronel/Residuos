package com.example.residuos.localdata

import android.content.Context
import com.example.residuos.localdata.database.AppDatabase

class UserRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val userDao = db.userDao()

}