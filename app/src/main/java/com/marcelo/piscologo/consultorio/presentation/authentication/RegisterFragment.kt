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
import com.marcelo.piscologo.consultorio.databinding.FragmentRegisterBinding
import com.marcelo.piscologo.consultorio.presentation.USER_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        binding.bntRegister.setOnClickListener {
            viewModel.register(
                email = binding.etRegisterEmail.text.toString(),
                password = binding.etRegisterPassword.text.toString(),
                user = getUserObj()
            )
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.register.collect { state ->
                when (state) {
                    is AuthenticationState.Loading -> {
                        binding.bntRegister.text = ""
                        binding.registerProgress.visibility = View.VISIBLE
                    }

                    is AuthenticationState.Failure -> {
                        binding.bntRegister.text =
                            resources.getString(R.string.register_create_account_button)
                        binding.registerProgress.visibility = View.GONE
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    is AuthenticationState.Success -> {
                        binding.bntRegister.text =
                            resources.getString(R.string.register_create_account_button)
                        binding.registerProgress.visibility = View.GONE
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

    private fun getUserObj(): User {
        return User(
            id = "",
            name = binding.etRegisterName.text.toString(),
            surname = binding.etRegisterSurname.text.toString(),
            email = binding.etRegisterEmail.text.toString(),
        )
    }
}
