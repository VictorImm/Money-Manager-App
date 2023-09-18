package com.example.money_manager_remake.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.money_manager_remake.MainActivity
import com.example.money_manager_remake.R
import com.example.money_manager_remake.adapter.OutcomeAdapter
import com.example.money_manager_remake.data.application.Application
import com.example.money_manager_remake.data.viewmodel.IncomeViewModel
import com.example.money_manager_remake.data.viewmodel.IncomeViewModelFactory
import com.example.money_manager_remake.data.viewmodel.OutcomeViewModel
import com.example.money_manager_remake.data.viewmodel.OutcomeViewModelFactory
import com.example.money_manager_remake.databinding.FragmentManagerMainBinding
import com.example.money_manager_remake.method.NumberFormatter
import com.example.money_manager_remake.ui.income.IncomeMainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ManagerMain : Fragment() {

    // binding
    private lateinit var binding: FragmentManagerMainBinding

    // widgets
    private lateinit var btnEdit: ImageView
    private lateinit var tvDate: TextView
    private lateinit var tvExpenseLeft: TextView
    private lateinit var tvExpenseSpent: TextView
    private lateinit var tvExpenseTotal: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvExpense: RecyclerView

    // properties variable
    private var income: Int = 0
    private var outcome: Int = 0

    // viewModel
    private val outcomeViewModel: OutcomeViewModel by viewModels {
        OutcomeViewModelFactory(
            (activity?.application as Application).outcomeDatabase.outcomeDao()
        )
    }
    private val incomeViewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory(
            (activity?.application as Application).incomeDatabase.incomeDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManagerMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // edit income if any
        btnEdit = binding.btnEdit
        btnEdit.setOnClickListener{
            // set property, 1 = edit
            IncomeMainActivity.inModeId = 1

            val intent = Intent(requireContext(), IncomeMainActivity::class.java)
            startActivity(intent)
        }

        // bind date and set date today
        tvDate = binding.textDate
        tvDate.text = "${MainActivity.DATE} - ${MainActivity.MONTH} - ${MainActivity.YEAR}"

        // bind progress bar
        progressBar = binding.progressBar

        // bind expense and calculate
        tvExpenseTotal = binding.textBudget
        tvExpenseSpent = binding.textSpent
        tvExpenseLeft = binding.textRpLeft

        incomeViewModel.retrieveIncome(
            MainActivity.MONTH,
            MainActivity.YEAR
        ).observe(this.viewLifecycleOwner) { listIncome ->

            outcomeViewModel.retrieveTotalOutcome(
                MainActivity.MONTH,
                MainActivity.YEAR
            ).observe(this.viewLifecycleOwner) { listOutcome ->
                // cache all data
                income = incomeViewModel.calculateIncome(listIncome)
                outcome = outcomeViewModel.calculateOutcome(listOutcome)

                // show data
                tvExpenseTotal.text = "Rp ${NumberFormatter.format(income)}"
                tvExpenseSpent.text = "Rp ${NumberFormatter.format(outcome)}"
                tvExpenseLeft.text = "Rp ${NumberFormatter.format(income - outcome)}"

                // progress bar progress
                ObjectAnimator.ofInt(progressBar, "progress", outcome)
                    .setDuration(2000)
                    .start()

                // set progress bar max
                progressBar.max = income

                // change color if outcome is greater than income
                if (outcome > income) {
                    progressBar.progressTintList = ColorStateList.valueOf(Color.RED)
                }
            }
        }

        // bind recyclerview
        rvExpense = binding.rvExpense
        rvExpense.setHasFixedSize(true)
        outcomeViewModel.retrieveOutcome(
            MainActivity.DATE,
            MainActivity.MONTH,
            MainActivity.YEAR
        ).observe(this.viewLifecycleOwner) {
            showRecyclerList()
        }
    }

    private fun showRecyclerList() {
        rvExpense.layoutManager = LinearLayoutManager(this.context)

        // show recyclerview from list of outcome
        val itemAdapter = OutcomeAdapter(outcomeViewModel.outcomes)
        rvExpense.adapter = itemAdapter
    }
}