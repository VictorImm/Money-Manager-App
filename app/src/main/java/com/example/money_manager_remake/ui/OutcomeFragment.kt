package com.example.money_manager_remake.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.money_manager_remake.MainActivity
import com.example.money_manager_remake.data.application.Application
import com.example.money_manager_remake.data.viewmodel.OutcomeViewModel
import com.example.money_manager_remake.data.viewmodel.OutcomeViewModelFactory
import com.example.money_manager_remake.databinding.FragmentOutcomeBinding
import com.google.android.material.textfield.TextInputEditText

class OutcomeFragment : Fragment() {

    private var typeId: Int? = null

    // binding
    private lateinit var binding: FragmentOutcomeBinding

    // widgets
    private lateinit var inputDate: TextInputEditText
    private lateinit var inputOutcome: TextInputEditText
    private lateinit var inputPrice: TextInputEditText
    private lateinit var btnAdd: Button

    // viewModel
    private val viewModel: OutcomeViewModel by viewModels {
        OutcomeViewModelFactory(
            (activity?.application as Application).outcomeDatabase.outcomeDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            typeId = OutcomeFragmentArgs.fromBundle(it).type
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOutcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputDate = binding.inputDate
        inputOutcome = binding.inputOutcome
        inputPrice = binding.inputPrice

        btnAdd = binding.btnAddOutcome
        btnAdd.setOnClickListener {
            inputReceiver(typeId!!)
        }
    }

    private fun inputReceiver(type: Int) {
        Log.d("inputReceiver", "input is being checked")
        // check if entry is valid
        if (viewModel.isEntryValid(
                MainActivity.DATE,
                MainActivity.MONTH,
                MainActivity.YEAR,
                inputOutcome.text.toString(),
                inputPrice.text.toString().toInt()
        )) {
            Log.d("inputReceiver", "input try insert")
            viewModel.addNewOutcome(
                MainActivity.DATE,
                MainActivity.MONTH,
                MainActivity.YEAR,
                type,
                inputOutcome.text.toString(),
                inputPrice.text.toString().toInt()
            )

            Log.d("inputReceiver", "input inserted")

            // moving fragment
            val action = OutcomeFragmentDirections.actionOutcomeFragmentToManagerMain()
            this.findNavController().navigate(action)

        } else {
            // TODO: make some error toast
        }
    }
}
