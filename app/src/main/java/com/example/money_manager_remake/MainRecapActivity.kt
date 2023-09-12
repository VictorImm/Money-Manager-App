package com.example.money_manager_remake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.money_manager_remake.databinding.ActivityMainRecapBinding

class MainRecapActivity : AppCompatActivity() {

    // binding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainRecapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get nav host fragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // instantiate the navController using navHostFragment
        navController = navHostFragment.navController

        // make sure actions in the ActionBAr get propagated to the NavController
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}