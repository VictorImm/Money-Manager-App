package com.example.money_manager_remake.data.income

import android.icu.text.DateFormat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income")
data class Income (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "d")
    val d: Int,
    @ColumnInfo(name = "m")
    val m: Int,
    @ColumnInfo(name = "y")
    val y: Int,

    @ColumnInfo(name = "income")
    val inc: Int
)
