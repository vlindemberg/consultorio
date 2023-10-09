package com.marcelo.piscologo.consultorio.presentation.authentication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.marcelo.piscologo.consultorio.R
import com.marcelo.piscologo.consultorio.data.model.User
import com.marcelo.piscologo.consultorio.databinding.FragmentSettingsBinding
import com.marcelo.piscologo.consultorio.utils.Session
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: AuthenticationViewModel by viewModels()
    private val user: User? = Session.getLoggedUser()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupObservers()
    }

    private fun setupData() {
        binding.tvName.text = buildString {
            append(user?.name)
            append(user?.surname)
        }
        binding.tvEmail.text = user?.email
        user?.photo?.let { url ->
            handlePhotoUrl(url)
        }
    }

    private fun handlePhotoUrl(url: String) = Glide.with(binding.ivProfile.context)
        .load(url)
        .placeholder(R.drawable.ic_profile_defaut)
        .into(binding.ivProfile)

    private val uploadPhotoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK
            && result.data != null
        ) {
            val photoUri = result.data!!.data
            photoUri?.let { uri ->
                user?.let { user ->
                    viewModel.uploadPhoto(uri, user)
                }
            }
        }
    }

    private fun setupObservers() {
        handlerProfilePhotoClick()
        handlerLogoutClick()
        uploadProfilePhotoObserver()
        logoutObserver()
    }

    private fun handlerProfilePhotoClick() {
        binding.ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            uploadPhotoLauncher.launch(intent)
        }
    }

    private fun handlerLogoutClick() {
        binding.pbLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun logoutObserver() {
        lifecycleScope.launch {
            viewModel.logout.collect { state ->
                when (state) {
                    is AuthenticationState.Loading -> {
                        binding.settingsLoad.visibility = View.VISIBLE
                    }

                    is AuthenticationState.Failure -> {
                        binding.settingsLoad.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    is AuthenticationState.Success -> {
                        binding.settingsLoad.visibility = View.INVISIBLE
                        findNavController().navigate(
                            SettingsFragmentDirections.actionSettingsFragmentToLoginFragment()
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    private fun uploadProfilePhotoObserver() {
        lifecycleScope.launch {
            viewModel.uploadPhoto.collect { state ->
                when (state) {
                    is AuthenticationState.Loading -> {
                        binding.settingsLoad.visibility = View.VISIBLE
                    }

                    is AuthenticationState.Failure -> {
                        binding.settingsLoad.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    is AuthenticationState.Success -> {
                        val user = state.data as User
                        binding.settingsLoad.visibility = View.INVISIBLE
                        handlePhotoUrl(user.photo!!)
                    }

                    else -> {}
                }
            }
        }
    }
}
