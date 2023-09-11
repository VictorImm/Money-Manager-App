package com.example.money_manager_remake.data.outcome

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.money_manager_remake.data.income.IncomeRoomDatabase

@Database(
    entities = [Outcome::class],
    version = 1,
    exportSchema = false
)

abstract class OutcomeRoomDatabase: RoomDatabase() {
    abstract fun outcomeDao(): OutcomeDao

    companion object {
        @Volatile
        private var INSTANCE: OutcomeRoomDatabase? = null

        fun getDatabase(context: Context): OutcomeRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OutcomeRoomDatabase::class.java,
                    "outcome_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}