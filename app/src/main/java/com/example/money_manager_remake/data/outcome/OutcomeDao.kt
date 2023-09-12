package com.example.money_manager_remake.data.outcome

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OutcomeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(out: Outcome)

    // select all from today date
    @Query("SELECT * from outcome WHERE d = :d AND m = :m AND y = :y")
    fun getDaily(d: Int, m: Int, y: Int): Flow<List<Outcome>>

    // select all from this month
    @Query("SELECT * from outcome WHERE m = :m AND y = :y")
    fun getTotal(m: Int, y: Int): Flow<List<Outcome>>

    // select all from this month by type
    @Query("SELECT * from outcome WHERE type = :type AND m = :m AND y = :y")
    fun getTotalType(type: Int, m: Int, y: Int): Flow<List<Outcome>>
}