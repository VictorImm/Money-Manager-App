package com.example.money_manager_remake.data.income

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.money_manager_remake.data.outcome.Outcome
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(inc: Income)

    // select all from this month
    @Query("SELECT * from income WHERE m = :m AND y = :y")
    fun getTotal(m: Int, y: Int): Flow<List<Income>>
}