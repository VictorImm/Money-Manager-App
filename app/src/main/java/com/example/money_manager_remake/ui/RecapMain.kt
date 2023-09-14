package com.example.money_manager_remake.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.money_manager_remake.MainActivity
import com.example.money_manager_remake.adapter.RecapAdapter
import com.example.money_manager_remake.data.application.Application
import com.example.money_manager_remake.data.viewmodel.IncomeViewModel
import com.example.money_manager_remake.data.viewmodel.IncomeViewModelFactory
import com.example.money_manager_remake.data.viewmodel.OutcomeViewModel
import com.example.money_manager_remake.data.viewmodel.OutcomeViewModelFactory
import com.example.money_manager_remake.databinding.FragmentRecapMainBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.coroutines.flow.observeOn

class RecapMain : Fragment() {

    // binding
    private lateinit var binding: FragmentRecapMainBinding

    // widgets
    private lateinit var rvType: RecyclerView
    private lateinit var barGraph: GraphView

    // viewModel
    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory(
            (activity?.application as Application).incomeDatabase.incomeDao()
        )
    }
    private val outcomeViewModel: OutcomeViewModel by viewModels {
        OutcomeViewModelFactory(
            (activity?.application as Application).outcomeDatabase.outcomeDao()
        )
    }

    // properties variable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecapMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // bind bar graph
        barGraph = binding.barGraph
        outcomeViewModel.retrieveOutcomeDaily(
            MainActivity.MONTH,
            MainActivity.YEAR
        ).observe(this.viewLifecycleOwner) { outcomeList ->
            val dataPointArray = outcomeViewModel.calculateOutcomeDaily(outcomeList)
            val series = BarGraphSeries(
                dataPointArray.toTypedArray()
            )

            barGraph.addSeries(series)
        }
        // styling
        // activate horizontal zooming and scrolling
        barGraph.viewport.isScalable = true

        // activate horizontal scrolling
        barGraph.viewport.isScrollable = true

        // bind recyclerview
        // TODO: set month retrieval is dynamic, by dropdown
        rvType = binding.rvType
        rvType.setHasFixedSize(true)
        incomeViewModel.retrieveIncome(
            MainActivity.MONTH,
            MainActivity.YEAR
        ).observe(this.viewLifecycleOwner) { listIncome ->

            outcomeViewModel.retrieveTotalOutcome(
                MainActivity.MONTH,
                MainActivity.YEAR
            ).observe(this.viewLifecycleOwner) { listOutcome ->
                // cache all data
                val income = incomeViewModel.calculateIncome(listIncome)
                val outcome = outcomeViewModel.calculateOutcomeType(listOutcome)

                // show data
                showRecyclerList(income, outcome)
            }
        }
    }

    private fun showRecyclerList(i: Int, o: HashMap<Int, Int>) {
        rvType.layoutManager = LinearLayoutManager(this.context)

        // show recyclerview from list of type
        val itemAdapter = RecapAdapter(i, o)
        rvType.adapter = itemAdapter
    }
}