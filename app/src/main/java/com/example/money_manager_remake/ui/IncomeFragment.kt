package com.example.money_manager_remake.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.money_manager_remake.MainActivity
import com.example.money_manager_remake.R
import com.example.money_manager_remake.data.application.Application
import com.example.money_manager_remake.data.viewmodel.IncomeViewModel
import com.example.money_manager_remake.data.viewmodel.IncomeViewModelFactory
import com.example.money_manager_remake.databinding.FragmentIncomeBinding
import com.google.android.material.textfield.TextInputEditText

class IncomeFragment : Fragment() {

    private var inModeId: Int? = null

    // binding
    private lateinit var binding: FragmentIncomeBinding

    // widgets
    private lateinit var inputDate: TextInputEditText
    private lateinit var inputIncome: TextInputEditText
    private lateinit var btnAdd: Button

    // viewModel
    private val viewModel: IncomeViewModel by viewModels {
        IncomeViewModelFactory(
            (activity?.application as Application).incomeDatabase.incomeDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            inModeId = IncomeFragmentArgs.fromBundle(it).incomeMode
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputDate = binding.inputDate
        inputIncome = binding.inputIncome
        btnAdd = binding.btnAddIncome
        btnAdd.text =
            when (inModeId) {
                0 -> "Add"
                else -> "Edit"
        }

        btnAdd.setOnClickListener {
            // save input to room database
            inputReceiver()
        }
    }

    private fun inputReceiver() {
        // check if input is valid
        if (viewModel.isEntryValid(
                MainActivity.DATE,
                MainActivity.MONTH,
                MainActivity.YEAR,
                inputIncome.text.toString().toInt()
        )) {
            viewModel.addNewIncome(
                MainActivity.DATE,
                MainActivity.MONTH,
                MainActivity.YEAR,
                inputIncome.text.toString().toInt()
            )

            val action = IncomeFragmentDirections.actionIncomeFragmentToManagerMain()
            this.findNavController().navigate(action)
        } else {
            // TODO: toast error
        }
    }
}