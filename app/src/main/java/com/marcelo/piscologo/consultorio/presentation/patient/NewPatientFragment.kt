package com.marcelo.piscologo.consultorio.presentation.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.marcelo.piscologo.consultorio.R
import com.marcelo.piscologo.consultorio.data.model.Address
import com.marcelo.piscologo.consultorio.data.model.Anamnesis
import com.marcelo.piscologo.consultorio.data.model.Patient
import com.marcelo.piscologo.consultorio.databinding.FragmentNewPatientBinding
import com.marcelo.piscologo.consultorio.utils.MaskType
import com.marcelo.piscologo.consultorio.utils.Session
import com.marcelo.piscologo.consultorio.utils.insertMask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewPatientFragment : Fragment() {

    private lateinit var binding: FragmentNewPatientBinding
    private val viewModel: PatientViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPatientBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatesSelectView()
        setupInputMasks()
        setupObservers()
    }

    private fun navigateToPatients() {
        findNavController().navigate(R.id.patientsFragment)
    }

    private fun setupObservers() {
        binding.newPatientBnt.setOnClickListener {
            viewModel.addPatient(getPatientInfo())
        }
        lifecycleScope.launch {
            viewModel.newPatient.collect { state ->
                when (state) {
                    is PatientState.Loading -> {
                        binding.newPatientProgress.visibility = View.VISIBLE
                        binding.newPatientBnt.isEnabled = false
                    }

                    is PatientState.Failure -> {
                        binding.newPatientProgress.visibility = View.GONE
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                    is PatientState.Success -> {
                        binding.newPatientProgress.visibility = View.GONE
                        navigateToPatients()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getPatientInfo(): Patient {
        val groupId = binding.rgGender.checkedRadioButtonId
        val name = binding.etNewPatientName.text.toString()
        val birthDate = binding.etNewPatientBirthDate.text.toString()
        val age = binding.etNewPatientAge.text.toString()
        val sex = binding.root.findViewById<RadioButton>(groupId).text.toString()
        val street = binding.etNewPatientStreet.text.toString()
        val district = binding.etNewPatientDistrict.text.toString()
        val city = binding.etNewPatientCity.text.toString()
        val civilStatus = binding.tvNewPatientCivilStatus.text.toString()
        val state = binding.tvNewPatientStates.text.toString()
        val cep = binding.etNewPatientCep.text.toString()
        val number = binding.etNewPatientNumber.text.toString()
        val email = binding.etNewPatientEmail.text.toString()
        val phone = binding.etNewPatientPhone.text.toString()
        val secondPhone = binding.etNewPatientSecondaryPhone.text.toString()
        val userId = Session.getLoggedUser()?.id.orEmpty()
        return Patient(
            id = "",
            name = name,
            birthDate = birthDate,
            age = age,
            civilStatus = civilStatus,
            gender = sex,
            address = Address(
                id = "",
                street = street,
                district = district,
                city = city,
                state = state,
                cep = cep,
                number = number
            ),
            phoneNumber = phone,
            secondaryPhoneNumber = secondPhone,
            email = email,
            anamnesis = Anamnesis(
                id = ""
            ),
            userId = userId
        )
    }

    private fun setupStatesSelectView() {
        val states = resources.getStringArray(R.array.states)
        val civilStatus = resources.getStringArray(R.array.civil_status)
        val stateArrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, states)
        val civilStatusArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, civilStatus)
        binding.tvNewPatientStates.setAdapter(stateArrayAdapter)
        binding.tvNewPatientCivilStatus.setAdapter(civilStatusArrayAdapter)
    }

    private fun setupInputMasks() {
        binding.etNewPatientPhone.insertMask(MaskType.PhoneNineWithDDD)
        binding.etNewPatientSecondaryPhone.insertMask(MaskType.PhoneNineWithDDD)
        binding.etNewPatientCep.insertMask(MaskType.CEP)
        binding.etNewPatientBirthDate.insertMask(MaskType.DATE)
    }

}