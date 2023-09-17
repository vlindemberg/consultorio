package com.marcelo.piscologo.consultorio.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.marcelo.piscologo.consultorio.R
import com.marcelo.piscologo.consultorio.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        setupNavMenu(navController)
    }

    private fun setupNavMenu(navigationController: NavController) {
        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment ||
                destination.id == R.id.patientsFragment ||
                destination.id == R.id.settingsFragment
            ) {
                binding.bottomNavMenu.visibility = View.VISIBLE
            } else {
                binding.bottomNavMenu.visibility = View.GONE
            }
        }
        setupBottomNavMenuListener()
    }

    private fun setupBottomNavMenuListener() {
        binding.bottomNavMenu.setOnItemSelectedListener {
            setupFragmentsNavigation(it)
        }
    }

    private fun setupFragmentsNavigation(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.agenda -> navController.navigate(R.id.homeFragment)
            R.id.patient -> navController.navigate(R.id.patientsFragment)
            R.id.settings -> navController.navigate(R.id.settingsFragment)
            else -> {}
        }
        return true
    }
}