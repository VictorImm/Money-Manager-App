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
import com.example.money_manager_remake.MainRecapActivity
import com.example.money_manager_remake.R
import com.example.money_manager_remake.adapter.OutcomeAdapter
import com.example.money_manager_remake.data.application.Application
import com.example.money_manager_remake.data.viewmodel.IncomeViewModel
import com.example.money_manager_remake.data.viewmodel.IncomeViewModelFactory
import com.example.money_manager_remake.data.viewmodel.OutcomeViewModel
import com.example.money_manager_remake.data.viewmodel.OutcomeViewModelFactory
import com.example.money_manager_remake.databinding.FragmentManagerMainBinding
import com.example.money_manager_remake.method.NumberFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ManagerMain : Fragment() {

    // binding
    private lateinit var binding: FragmentManagerMainBinding

    // widgets
    private lateinit var floatingAdd: FloatingActionButton
    private lateinit var floatingOut: FloatingActionButton
    private lateinit var floatingInc: FloatingActionButton
    private lateinit var floatingRec: FloatingActionButton

    private lateinit var btnEdit: ImageView
    private lateinit var tvDate: TextView
    private lateinit var tvExpenseLeft: TextView
    private lateinit var tvExpenseSpent: TextView
    private lateinit var tvExpenseTotal: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvExpense: RecyclerView

    // animation
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom) }

    // properties variable
    private var clicked = false

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
            // TODO: add type, mau bertipe edit atau add
            val action = ManagerMainDirections.actionManagerMainToIncomeFragment(
                incomeMode = 1 // income edit mode
            )
            this.findNavController().navigate(action)
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

        floatingAdd = binding.floatingAdd
        floatingOut = binding.floatingOutcome
        floatingInc = binding.floatingIncome
        floatingRec = binding.floatingRecap

        floatingAdd.setOnClickListener {
            onAddButtonClick()
        }
        floatingOut.setOnClickListener {
            Toast.makeText(context, "Outcome Button Clicked !", Toast.LENGTH_SHORT).show()
            val action = ManagerMainDirections.actionManagerMainToTypeFragment()
            this.findNavController().navigate(action)
            clicked = false
        }
        floatingInc.setOnClickListener {
            Toast.makeText(context, "Income Button Clicked !", Toast.LENGTH_SHORT).show()
            val action = ManagerMainDirections.actionManagerMainToIncomeFragment(
                incomeMode = 0 // income add mode
            )
            this.findNavController().navigate(action)
            clicked = false
        }
        floatingRec.setOnClickListener {
            Toast.makeText(context, "Recap Button Clicked !", Toast.LENGTH_SHORT).show()

            val intent = Intent(this.context, MainRecapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onAddButtonClick() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            floatingOut.visibility = View.VISIBLE
            floatingInc.visibility = View.VISIBLE
            floatingRec.visibility = View.VISIBLE
        } else {
            floatingOut.visibility = View.INVISIBLE
            floatingInc.visibility = View.INVISIBLE
            floatingRec.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            floatingOut.startAnimation(fromBottom)
            floatingInc.startAnimation(fromBottom)
            floatingRec.startAnimation(fromBottom)
            floatingAdd.startAnimation(rotateOpen)
        } else {
            floatingOut.startAnimation(toBottom)
            floatingInc.startAnimation(toBottom)
            floatingRec.startAnimation(toBottom)
            floatingAdd.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            floatingOut.isClickable = true
            floatingInc.isClickable = true
            floatingRec.isClickable = true
        } else {
            floatingOut.isClickable = false
            floatingInc.isClickable = false
            floatingRec.isClickable = false
        }
    }

    private fun showRecyclerList() {
        rvExpense.layoutManager = LinearLayoutManager(this.context)

        // show recyclerview from list of outcome
        val itemAdapter = OutcomeAdapter(outcomeViewModel.outcomes)
        rvExpense.adapter = itemAdapter
    }
}