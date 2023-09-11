package com.example.money_manager_remake.method

import android.icu.text.NumberFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class NumberFormatter {
    companion object {
        @RequiresApi(Build.VERSION_CODES.N)
        private val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        fun format(n: Int): String {
            return numberFormat.format(n)
        }
    }
}