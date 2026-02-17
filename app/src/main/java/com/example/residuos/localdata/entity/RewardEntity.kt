package com.example.residuos.localdata.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class RewardEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val descripcion: String,

    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int
)
