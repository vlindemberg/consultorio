package com.marcelo.piscologo.consultorio.presentation.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marcelo.piscologo.consultorio.R
import com.marcelo.piscologo.consultorio.data.model.Patient
import com.marcelo.piscologo.consultorio.databinding.FragmentPatientsBinding
import com.marcelo.piscologo.consultorio.presentation.USER_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PatientsFragment : Fragment() {

    private lateinit var binding: FragmentPatientsBinding
    private val viewModel: PatientViewModel by viewModels()

    private lateinit var patientsAdapter: PatientsAdapter
    private lateinit var patientsList: List<Patient>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPatients(arguments?.getString(USER_ID).orEmpty())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearch()
        setupObservers()
    }

    private fun setupObservers() {
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.newPatientFragment)
        }
        lifecycleScope.launch {
            viewModel.patients.collect { state ->
                when (state) {
                    is PatientState.Loading -> {
                        binding.patientsLoad.visibility = View.VISIBLE
                        binding.nscPatients.visibility = View.GONE
                        binding.ivEmptyList.visibility = View.VISIBLE
                    }

                    is PatientState.Failure -> {
                        binding.patientsLoad.visibility = View.GONE
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                    is PatientState.Success -> {
                        binding.patientsLoad.visibility = View.GONE
                        setupAdapter(state.patients)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupAdapter(patients: List<Patient>) {
        if (patients.isEmpty()) {
            binding.nscPatients.visibility = View.GONE
            binding.ivEmptyList.visibility = View.VISIBLE
        } else {
            patientsList = patients
            patientsAdapter = PatientsAdapter(patients, onItemClickListener = {
                navigateToPatientsDetails(it.id)
            })
            binding.patientsRecyclerView.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = patientsAdapter
                isClickable = true

            }
            binding.nscPatients.visibility = View.VISIBLE
            binding.ivEmptyList.visibility = View.GONE
        }
    }

    private fun navigateToPatientsDetails(patientId: String) {
        TODO("Not yet implemented")
    }

    private fun setupSearch() {
        binding.patientsSearchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    setupFilterList(newText)
                    return true
                }
            })
    }

    private fun setupFilterList(text: String?) {
        val filteredList = viewModel.filterList(text, patientsList)
        if (filteredList.isEmpty()) {
            binding.constraintLayout.visibility = View.GONE
            binding.ivEmptyList.visibility = View.VISIBLE
            binding.nscPatients.visibility = View.GONE
        } else {
            binding.constraintLayout.visibility = View.VISIBLE
            binding.ivEmptyList.visibility = View.GONE
            binding.nscPatients.visibility = View.VISIBLE
            patientsAdapter.setFilteredList(filteredList)
        }
    }
}
