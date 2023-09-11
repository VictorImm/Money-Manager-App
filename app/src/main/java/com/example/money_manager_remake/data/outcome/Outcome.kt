package com.example.money_manager_remake.data.outcome

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outcome")
data class Outcome (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "d")
    val d: Int,
    @ColumnInfo(name = "m")
    val m: Int,
    @ColumnInfo(name = "y")
    val y: Int,

    @ColumnInfo(name = "type")
    val type: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "price")
    val price: Int
)