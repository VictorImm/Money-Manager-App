package com.example.money_manager_remake.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.money_manager_remake.R
import com.example.money_manager_remake.databinding.FragmentTypeBinding

class TypeFragment : Fragment() {

    // binding
    private lateinit var binding: FragmentTypeBinding

    // widget
    private lateinit var btnHealth: Button
    private lateinit var btnFnb: Button
    private lateinit var btnTransport: Button
    private lateinit var btnEew: Button
    private lateinit var btnLaundry: Button
    private lateinit var btnEnter: Button
    private lateinit var btnOther: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonListener()
    }

    private fun buttonListener() {
        btnHealth = binding.btnHealth
        btnFnb = binding.btnFnb
        btnTransport = binding.btnTransport
        btnEew = binding.btnEew
        btnLaundry = binding.btnLaundry
        btnEnter = binding.btnEnter
        btnOther = binding.btnOther

        btnHealth.setOnClickListener{
            val action = TypeFragmentDirections.actionTypeFragmentToOutcomeFragment(
                type = 1
            )
            this.findNavController().navigate(action)
        }
        btnFnb.setOnClickListener{
            val action = TypeFragmentDirections.actionTypeFragmentToOutcomeFragment(
                type = 2
            )
            this.findNavController().navigate(action)
        }
        btnTransport.setOnClickListener{
            val action = TypeFragmentDirections.actionTypeFragmentToOutcomeFragment(
                type = 3
            )
            this.findNavController().navigate(action)
        }
        btnEew.setOnClickListener{
            val action = TypeFragmentDirections.actionTypeFragmentToOutcomeFragment(
                type = 4
            )
            this.findNavController().navigate(action)
        }
        btnLaundry.setOnClickListener{
            val action = TypeFragmentDirections.actionTypeFragmentToOutcomeFragment(
                type = 5
            )
            this.findNavController().navigate(action)
        }
        btnEnter.setOnClickListener{
            val action = TypeFragmentDirections.actionTypeFragmentToOutcomeFragment(
                type = 6
            )
            this.findNavController().navigate(action)
        }
        btnOther.setOnClickListener{
            val action = TypeFragmentDirections.actionTypeFragmentToOutcomeFragment(
                type = 7
            )
            this.findNavController().navigate(action)
        }
    }
}