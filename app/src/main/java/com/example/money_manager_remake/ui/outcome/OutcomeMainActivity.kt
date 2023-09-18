package com.example.money_manager_remake.ui.outcome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.money_manager_remake.R
import com.example.money_manager_remake.databinding.ActivityOutcomeMainBinding

class OutcomeMainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityOutcomeMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutcomeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get nav host fragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // instantiate the navController using navHostFragment
        navController = navHostFragment.navController

        // make sure action in this ActionBar get propagated to the NAvController
        setupActionBarWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}