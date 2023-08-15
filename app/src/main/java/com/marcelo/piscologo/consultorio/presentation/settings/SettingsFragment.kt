package com.marcelo.piscologo.consultorio.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.marcelo.piscologo.consultorio.R
import com.marcelo.piscologo.consultorio.databinding.FragmentSettingsBinding
import com.marcelo.piscologo.consultorio.presentation.authentication.AuthenticationState
import com.marcelo.piscologo.consultorio.presentation.authentication.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        binding.logout.setOnClickListener {
            viewModel.logout()
        }
        lifecycleScope.launch {
            viewModel.logout.collect { state ->
                when (state) {
                    is AuthenticationState.Loading -> {
                        binding.logout.text = ""
                        binding.logoutProgress.visibility = View.VISIBLE
                    }

                    is AuthenticationState.Failure -> {
                        binding.logout.text = resources.getString(R.string.logout)
                        binding.logoutProgress.visibility = View.GONE
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    is AuthenticationState.Success -> {
                        binding.logout.text = resources.getString(R.string.logout)
                        binding.logoutProgress.visibility = View.GONE
                        findNavController().navigate(
                            R.id.loginFragment
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}
