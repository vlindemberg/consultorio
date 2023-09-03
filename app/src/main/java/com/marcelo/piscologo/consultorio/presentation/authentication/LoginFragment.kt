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
import com.marcelo.piscologo.consultorio.databinding.FragmentLoginBinding
import com.marcelo.piscologo.consultorio.presentation.USER_ID
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
                        navigateToHome()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun navigateToHome() {
        val bundle = Bundle()
        bundle.putString(USER_ID, viewModel.currentUser?.uid.orEmpty())
        findNavController().navigate(R.id.homeFragment, bundle)
    }
}
