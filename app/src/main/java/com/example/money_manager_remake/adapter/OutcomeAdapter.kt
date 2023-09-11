package com.example.money_manager_remake.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.money_manager_remake.R
import com.example.money_manager_remake.data.outcome.Outcome
import com.example.money_manager_remake.data.viewmodel.OutcomeViewModel

class OutcomeAdapter(
    private val listOutcome: LiveData<List<Outcome>>
): RecyclerView.Adapter<OutcomeAdapter.ListViewHolder>() {

    class ListViewHolder(outcomeView: View): RecyclerView.ViewHolder(outcomeView) {
        val tvName: TextView = itemView.findViewById(R.id.text_name)
        val tvType: TextView = itemView.findViewById(R.id.text_type)
        val tvPrice: TextView = itemView.findViewById(R.id.text_price)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.row_expense,
                    parent,
                    false
                )
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listOutcome.value?.size?:0

    override fun onBindViewHolder(holder: OutcomeAdapter.ListViewHolder, position: Int) {
        val expense = listOutcome.value?.get(position)

        if (expense != null) {
            holder.tvName.text = expense.name
            holder.tvType.text =
                when (expense.type) {
                    1 -> "Healthcare"
                    2 -> "Food and Drink"
                    3 -> "Transportation"
                    4 -> "Electric, Electronic, and Water"
                    5 -> "Laundry and Clean"
                    6 -> "Entertainment"
                    else -> "Other"
                }
            holder.tvPrice.text = "Rp ${expense.price}"
        }
    }

    init {
        // observe live data
        listOutcome.observeForever {
            // notify if any data change
            notifyDataSetChanged()
        }
    }

}