package com.example.money_manager_remake.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.money_manager_remake.R
import com.example.money_manager_remake.method.NumberFormatter

class RecapAdapter(
    private val income: Int,
    private val listOutcome: HashMap<Int, Int>
): RecyclerView.Adapter<RecapAdapter.ListViewHolder>() {

    class ListViewHolder(recapView: View): RecyclerView.ViewHolder(recapView) {
        val tvType: TextView = itemView.findViewById(R.id.text_type)
        val tvTotal: TextView = itemView.findViewById(R.id.text_total)
        val tvPercent: TextView = itemView.findViewById(R.id.text_percentage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.row_recap_type,
                    parent,
                    false
                )
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listOutcome.size

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecapAdapter.ListViewHolder, position: Int) {
        val type = listOutcome.entries.elementAtOrNull(position)

        if (type != null) {
            holder.tvType.text =
                when (type.key) {
                    1 -> "Healthcare"
                    2 -> "Food and Drink"
                    3 -> "Transportation"
                    4 -> "Electric, Electronic, and Water"
                    5 -> "Laundry and Clean"
                    6 -> "Entertainment"
                    7 -> "Other"
                    else -> "Income Left"
                }
            holder.tvTotal.text =
                when (type.key) {
                    8 -> "Rp ${NumberFormatter.format(income - type.value)}"
                    else -> "Rp ${NumberFormatter.format(type.value)}"
                }

            // TODO: do float .2f
            holder.tvPercent.text =
                when (type.key) {
                    8 -> "${(((income - type.value)*100)/income)}%"
                    else -> "${((type.value*100)/income)}%"
                }
        }
    }
}