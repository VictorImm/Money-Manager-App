package com.example.money_manager_remake.ui.outcome

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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

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

    // variable properties
    private var d: Int = MainActivity.DATE
    private var m: Int = MainActivity.MONTH
    private var y: Int = MainActivity.YEAR

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
        inputDate.setText("$d-$m-$y")
        inputDate.setOnClickListener { dateDialog() }

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
                d,
                m,
                y,
                inputOutcome.text.toString(),
                inputPrice.text.toString().toInt()
        )) {
            Log.d("inputReceiver", "input try insert")
            viewModel.addNewOutcome(
                d,
                m,
                y,
                type,
                inputOutcome.text.toString(),
                inputPrice.text.toString().toInt()
            )

            Log.d("inputReceiver", "input inserted")

            // moving fragment
            activity?.finish()

        } else {
            // TODO: make some error toast
        }
    }

    private fun dateDialog() {
        // build date picker
        val datePicker = MaterialDatePicker
            .Builder
            .datePicker()
            .build()

        datePicker.show(requireFragmentManager(), "DatePicker")

        // Setting up the event for when ok is clicked
        datePicker.addOnPositiveButtonClickListener {
            // formatting date
            val selectedDate = SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            ).format(Date(it))

            // show selected date to input layout
            inputDate.setText(selectedDate)

            // split data
            val dateParts = selectedDate.split("-")
            d = dateParts[0].toInt()
            m = dateParts[1].toInt()
            y = dateParts[2].toInt()
        }

        // Setting up the event for when cancelled is clicked
        datePicker.addOnNegativeButtonClickListener { }

        // Setting up the event for when back button is pressed
        datePicker.addOnCancelListener { }
    }
}
