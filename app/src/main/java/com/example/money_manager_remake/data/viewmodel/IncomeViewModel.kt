package com.example.money_manager_remake.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.money_manager_remake.data.income.Income
import com.example.money_manager_remake.data.income.IncomeDao
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class IncomeViewModel(private val incomeDao: IncomeDao): ViewModel() {

    // insert item
    fun addNewIncome(d: Int, m: Int, y: Int, inc: Int) {
        val newIncome = Income(
            d = d,
            m = m,
            y = y,
            inc = inc
        )
        viewModelScope.launch {
            incomeDao.insert(newIncome)
        }
    }

    fun isEntryValid(d: Int, m: Int, y: Int, inc: Int): Boolean {
        if (
            d.toString().isBlank() ||
            m.toString().isBlank() ||
            y.toString().isBlank() ||
            inc.toString().isBlank()
        ) {
            return false
        }
        return true
    }

    // retrieve item
    fun retrieveIncome(m: Int, y: Int): LiveData<List<Income>> {
        return incomeDao.getTotal(m, y).asLiveData()
    }
    fun calculateIncome(listIncome: List<Income>): Int {
        var income = 0

        for (i in listIncome) {
            income += i.inc
        }

        return income
    }
}

class IncomeViewModelFactory(private val incomeDao: IncomeDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomeViewModel(incomeDao) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}