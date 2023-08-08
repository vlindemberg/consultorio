package com.marcelo.piscologo.consultorio.presentation.authentication.login

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
import com.marcelo.piscologo.consultorio.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        binding.loginBtn.setOnClickListener {
            viewModel.login(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
        binding.registerLabel.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
        lifecycleScope.launch {
            viewModel.loginFlow.collect { state ->
                when (state) {
                    is LoginState.Loading -> {
                        binding.loginBtn.text = ""
                        binding.loginProgress.visibility = View.VISIBLE
                    }

                    is LoginState.Failure -> {
                        binding.loginBtn.text = resources.getString(R.string.login)
                        binding.loginProgress.visibility = View.GONE
                        binding.loginBtn.text = resources.getString(R.string.login)
                        binding.loginProgress.visibility = View.GONE
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    is LoginState.Success -> {
                        binding.loginBtn.text = resources.getString(R.string.login)
                        binding.loginProgress.visibility = View.GONE
                        findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToHomeNavigation()
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}
