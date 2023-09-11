package com.example.money_manager_remake.data.income

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Income::class],
    version = 1,
    exportSchema = false
)

abstract class IncomeRoomDatabase: RoomDatabase() {
    abstract fun incomeDao(): IncomeDao

    companion object {
        @Volatile
        private var INSTANCE: IncomeRoomDatabase? = null

        fun getDatabase(context: Context): IncomeRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IncomeRoomDatabase::class.java,
                    "income_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}