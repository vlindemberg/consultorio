package com.marcelo.piscologo.consultorio.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.marcelo.piscologo.consultorio.R
import com.marcelo.piscologo.consultorio.databinding.ActivityMainBinding
import com.marcelo.piscologo.consultorio.presentation.authentication.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

const val USER_ID = "userId"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedCallback()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        setupNavMenu(navController)
    }

    private fun setupNavMenu(navigationController: NavController) {
        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment ||
                destination.id == R.id.registerFragment ||
                destination.id == R.id.forgotPasswordFragment
            ) {
                setupAuthVisibility()
            } else {
                homeVisibility()
            }
        }
        setupBottomNavMenuListener()
        setupRailNavMenuListener()
    }

    private fun homeVisibility() {
        if (resources.getBoolean(R.bool.is_tablet)) {
            navMenuVisibilityForTablet()
        } else {
            navMenuVisibilityForPhone()
        }
    }

    private fun setupRailNavMenuListener() {
        binding.railNavMenu.setOnItemSelectedListener {
            setupFragmentsNavigation(it)
        }
    }

    private fun setupBottomNavMenuListener() {
        binding.bottomNavMenu.setOnItemSelectedListener {
            setupFragmentsNavigation(it)
        }
    }

    private fun setupFragmentsNavigation(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.agenda -> navigate(R.id.homeFragment)
            R.id.patient -> navigate(R.id.patientsFragment)
            R.id.settings -> navigate(R.id.settingsFragment)
            else -> {}
        }
        return true
    }

    private fun navigate(fragmentId: Int) {
        val bundle = Bundle()
        bundle.putString(USER_ID, viewModel.currentUser?.uid.orEmpty())
        navController.navigate(fragmentId, bundle)
    }

    private fun setupAuthVisibility() {
        binding.bottomNavMenu.visibility = View.GONE
        binding.railNavMenu.visibility = View.GONE
    }

    private fun navMenuVisibilityForPhone() {
        binding.bottomNavMenu.visibility = View.VISIBLE
        binding.railNavMenu.visibility = View.GONE
    }

    private fun navMenuVisibilityForTablet() {
        binding.bottomNavMenu.visibility = View.GONE
        binding.railNavMenu.visibility = View.VISIBLE
    }

    private fun onBackPressedCallback() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.loginFragment) {
                    moveTaskToBack(true)
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }

        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
}