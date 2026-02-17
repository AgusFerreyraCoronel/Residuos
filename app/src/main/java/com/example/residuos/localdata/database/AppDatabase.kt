package com.example.residuos.localdata.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.residuos.localdata.dao.UserDao
import com.example.residuos.localdata.entity.User
import com.example.residuos.localdata.entity.RewardEntity
import android.content.Context
import com.example.residuos.localdata.dao.RewardDao

@Database(entities = [User::class,
    RewardEntity::class ], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun rewardDao(): RewardDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
