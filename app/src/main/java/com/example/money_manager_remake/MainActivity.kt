package com.example.money_manager_remake

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.money_manager_remake.databinding.ActivityMainBinding
import com.example.money_manager_remake.ui.ManagerMainDirections
import com.example.money_manager_remake.ui.income.IncomeMainActivity
import com.example.money_manager_remake.ui.outcome.OutcomeMainActivity
import com.example.money_manager_remake.ui.recap.RecapMainFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    // navigation
    private lateinit var navController: NavController

    // binding
    private lateinit var binding: ActivityMainBinding

    // widgets
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var floatingAdd: FloatingActionButton
    private lateinit var floatingInc: FloatingActionButton
    private lateinit var floatingOut: FloatingActionButton

    // animation
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim)
    }
    private val fromBottomInc: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_inc)
    }
    private val fromBottomOut: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_out)
    }
    private val toBottomInc: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_inc)
    }
    private val toBottomOut: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_out)
    }

    // properties variable
    private var clicked = false

    companion object {
        var DATE: Int = 0
        var MONTH: Int = 0
        var YEAR: Int = 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get date today and parse it
        getDateToday()

        // get nav host fragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        //instantiate the navController using navHostFragment
        navController = navHostFragment.navController

        // make sure actions in the ActionBar get propagated to the NavController
        setupActionBarWithNavController(navController)

        // Bottom navigation
        bottomNavBar = binding.bottomNavBar
        bottomNavBar.menu.findItem(R.id.placeholder).isCheckable = false
        bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // go to home
                    val action = RecapMainFragmentDirections.actionRecapMainToManagerMain()
                    this.navController.navigate(action)
                }
                R.id.recap -> {
                    if (clicked) {
                        onAddButtonClick()
                    }
                    // go to recap fragment
                    val action = ManagerMainDirections.actionManagerMainToRecapMain()
                    this.navController.navigate(action)
                }
            }
            true
        }
        bottomNavBar.setOnItemReselectedListener { }

        // floating button
        floatingAdd = binding.fabAdd
        floatingInc = binding.fabIncome
        floatingOut = binding.fabOutcome

        floatingAdd.setOnClickListener { onAddButtonClick() }
        floatingInc.setOnClickListener {
            // set property, 0 = add
            IncomeMainActivity.inModeId = 0

            val intent = Intent(this, IncomeMainActivity::class.java)
            startActivity(intent)
        }
        floatingOut.setOnClickListener {
            val intent = Intent(this, OutcomeMainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateToday() {
        val localDate = LocalDate.now()

        DATE = localDate.dayOfMonth
        MONTH = localDate.monthValue
        YEAR = localDate.year
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
        } else {
            floatingOut.visibility = View.INVISIBLE
            floatingInc.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            floatingOut.startAnimation(fromBottomOut)
            floatingInc.startAnimation(fromBottomInc)
            floatingAdd.startAnimation(rotateOpen)
        } else {
            floatingOut.startAnimation(toBottomOut)
            floatingInc.startAnimation(toBottomInc)
            floatingAdd.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            floatingOut.isClickable = true
            floatingInc.isClickable = true
        } else {
            floatingOut.isClickable = false
            floatingInc.isClickable = false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // TODO: to debug
        // reset selection by go to home
        bottomNavBar.menu.findItem(R.id.home).isChecked = true
    }
}