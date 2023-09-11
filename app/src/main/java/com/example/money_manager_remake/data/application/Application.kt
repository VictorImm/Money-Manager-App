package com.example.money_manager_remake.data.application

import android.app.Application
import com.example.money_manager_remake.data.income.IncomeRoomDatabase
import com.example.money_manager_remake.data.outcome.OutcomeRoomDatabase

class Application: Application() {
    val incomeDatabase: IncomeRoomDatabase by lazy {
        IncomeRoomDatabase.getDatabase(applicationContext)
    }

    val outcomeDatabase: OutcomeRoomDatabase by lazy {
        OutcomeRoomDatabase.getDatabase(applicationContext)
    }
}