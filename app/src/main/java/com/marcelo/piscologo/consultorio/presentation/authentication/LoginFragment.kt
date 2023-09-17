package com.marcelo.piscologo.consultorio.presentation.authentication

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
import com.marcelo.piscologo.consultorio.data.model.User
import com.marcelo.piscologo.consultorio.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthenticationViewModel by viewModels()

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
        binding.forgotPassLabel.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            )
        }
        lifecycleScope.launch {
            viewModel.loginFlow.collect { state ->
                when (state) {
                    is AuthenticationState.Loading -> {
                        binding.loginBtn.text = ""
                        binding.loginProgress.visibility = View.VISIBLE
                    }

                    is AuthenticationState.Failure -> {
                        binding.loginBtn.text = resources.getString(R.string.login)
                        binding.loginProgress.visibility = View.GONE
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    is AuthenticationState.Success -> {
                        binding.loginBtn.text = resources.getString(R.string.login)
                        binding.loginProgress.visibility = View.GONE
                        setupSession(state.data as User)
                        navigateToHome()
                    }

                    else -> {}
                }
            }
        }
        lifecycleScope.launch {
            viewModel.validateSessionFlow.collect { state ->
                when (state) {
                    is AuthenticationState.Loading -> {
                        binding.sessionProgress.visibility = View.VISIBLE
                        binding.loginContainer.visibility = View.GONE
                    }

                    is AuthenticationState.Failure -> {
                        binding.loginBtn.text = resources.getString(R.string.login)
                        binding.loginProgress.visibility = View.GONE
                        binding.sessionProgress.visibility = View.VISIBLE
                        binding.loginContainer.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    is AuthenticationState.Success -> {
                        setupSession(state.data as User)
                        navigateToHome()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupSession(user: User) {
        viewModel.session(user)
    }

    private fun navigateToHome() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }
}
