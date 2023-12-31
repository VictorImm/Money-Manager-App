package com.example.money_manager_remake.data.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.money_manager_remake.MainActivity
import com.example.money_manager_remake.data.DateDistinctResult
import com.example.money_manager_remake.data.outcome.Outcome
import com.example.money_manager_remake.data.outcome.OutcomeDao
import com.jjoe64.graphview.series.DataPoint
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch

class OutcomeViewModel(private val outcomeDao: OutcomeDao): ViewModel() {

    // cache all data
    val outcomes: LiveData<List<Outcome>> = outcomeDao.getDaily(
        MainActivity.DATE,
        MainActivity.MONTH,
        MainActivity.YEAR
    ).asLiveData()

    // insert item
    fun addNewOutcome(d: Int, m: Int, y: Int, type: Int, name: String, price: Int) {
        val newOutcome = Outcome(
            d = d,
            m = m,
            y = y,
            type = type,
            name = name,
            price = price
        )
        viewModelScope.launch {
            outcomeDao.insert(newOutcome)
        }
    }

    fun isEntryValid(d: Int, m: Int, y: Int, name: String, price: Int): Boolean {
        Log.d("isEntryValid", "checking entry")
        if (
            d.toString().isBlank() ||
            m.toString().isBlank() ||
            y.toString().isBlank() ||
            name.isBlank() ||
            price.toString().isBlank()
        ) {
            return false
        }
        return true
    }

    // retrieve date
    fun retrieveOutcomeDaily(m: Int, y: Int): LiveData<List<DateDistinctResult>> {
        return outcomeDao.getDateDistinct(m, y).asLiveData()
    }

    // retrieve item
    fun retrieveOutcome(d: Int, m: Int, y: Int): LiveData<List<Outcome>> {
        return outcomeDao.getDaily(d, m, y).asLiveData()
    }
    fun retrieveTotalOutcome(m: Int, y: Int): LiveData<List<Outcome>> {
        return outcomeDao.getTotal(m, y).asLiveData()
    }
    fun calculateOutcome(listOutcome: List<Outcome>): Int {
        var outcome = 0

        for (o in listOutcome) {
            outcome += o.price
        }

        return outcome
    }
    fun calculateOutcomeType(listOutcome: List<Outcome>): HashMap<Int, Int> {
        var outcome: HashMap<Int, Int> = hashMapOf(
            1 to 0,
            2 to 0,
            3 to 0,
            4 to 0,
            5 to 0,
            6 to 0,
            7 to 0,
            8 to 0
        )


        for (o in listOutcome) {
            outcome[o.type] = outcome[o.type]!! + o.price
        }
        outcome[8] = outcome[8]!! + calculateOutcome(listOutcome)

        return outcome
    }
    fun calculateOutcomeDaily(listOutcome: List<DateDistinctResult>): ArrayList<DataPoint> {
        var prev = 0
        var totalPrice = 0
        val dataPointArray = ArrayList<DataPoint>()

        for (o in listOutcome) {

            if (o.d == prev) {
                totalPrice += o.price
            } else {
                dataPointArray.add(DataPoint(prev.toDouble(), totalPrice.toDouble()))
                totalPrice = o.price
            }
            prev = o.d
        }
        return dataPointArray
    }
}

class OutcomeViewModelFactory(private val outcomeDao: OutcomeDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OutcomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OutcomeViewModel(outcomeDao) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}