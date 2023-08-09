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
import com.marcelo.piscologo.consultorio.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.forgotPassBtn.setOnClickListener {
            viewModel.forgotPassword(binding.etForgotPassEmail.text.toString())
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.forgotPassword.collect { state ->
                when (state) {
                    is AuthenticationState.Loading -> {
                        binding.forgotPassBtn.text = ""
                        binding.forgotPassProgress.visibility = View.VISIBLE
                    }

                    is AuthenticationState.Failure -> {
                        binding.forgotPassBtn.text =
                            resources.getString(R.string.forgot_pass_button)
                        binding.forgotPassProgress.visibility = View.GONE
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    is AuthenticationState.Success -> {
                        binding.forgotPassBtn.text =
                            resources.getString(R.string.forgot_pass_button)
                        binding.forgotPassProgress.visibility = View.GONE
                        Toast.makeText(requireContext(), state.data.toString(), Toast.LENGTH_LONG)
                            .show()
                        findNavController().popBackStack()
                    }

                    else -> {}
                }
            }
        }
    }
}
