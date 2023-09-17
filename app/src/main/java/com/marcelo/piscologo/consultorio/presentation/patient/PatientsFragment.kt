package com.marcelo.piscologo.consultorio.presentation.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.marcelo.piscologo.consultorio.R
import com.marcelo.piscologo.consultorio.data.model.Patient
import com.marcelo.piscologo.consultorio.databinding.FragmentPatientsBinding
import com.marcelo.piscologo.consultorio.utils.Session
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PatientsFragment : Fragment() {

    private lateinit var binding: FragmentPatientsBinding
    private val viewModel: PatientViewModel by viewModels()

    private lateinit var patientsAdapter: PatientsAdapter
    private var patientsList: ArrayList<Patient> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPatientsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPatients(Session.getLoggedUser()?.id.orEmpty())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearch()
        setupObservers()
    }

    private fun setupObservers() {
        binding.floatingActionButton.setOnClickListener {
            findNavController()
                .navigate(PatientsFragmentDirections.actionPatientsFragmentToNewPatientFragment())
        }
        lifecycleScope.launch {
            viewModel.patients.collect { state ->
                when (state) {
                    is PatientState.Loading -> {
                        binding.patientsLoad.visibility = View.VISIBLE
                        binding.constraintLayout.visibility = View.GONE
                        binding.ivEmptyList.visibility = View.GONE
                    }

                    is PatientState.Failure -> {
                        binding.patientsLoad.visibility = View.GONE
                        binding.constraintLayout.visibility = View.GONE
                        binding.ivEmptyList.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                    is PatientState.Success -> {
                        binding.patientsLoad.visibility = View.GONE
                        patientsList.addAll(state.patients)
                        setupAdapter(patientsList)
                    }

                    else -> {}
                }
            }
        }
        lifecycleScope.launch {
            viewModel.removePatient.collect { state ->
                when (state) {
                    is PatientState.Loading -> {
                        binding.patientsLoad.visibility = View.VISIBLE
                        binding.constraintLayout.visibility = View.GONE
                        binding.ivEmptyList.visibility = View.GONE
                    }

                    is PatientState.Failure -> {
                        binding.patientsLoad.visibility = View.GONE
                        binding.constraintLayout.visibility = View.VISIBLE
                        binding.ivEmptyList.visibility = View.GONE
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                    is PatientState.Success -> {
                        binding.patientsLoad.visibility = View.GONE
                        binding.constraintLayout.visibility = View.VISIBLE
                        binding.ivEmptyList.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            state.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupAdapter(patients: ArrayList<Patient>) {
        if (patients.isEmpty()) {
            binding.constraintLayout.visibility = View.GONE
            binding.ivEmptyList.visibility = View.VISIBLE
        } else {
            patientsList = patients
            patientsAdapter = PatientsAdapter(
                patientList = patients,
                onItemClickListener = { navigateToPatientsDetails(it.id) },
                onItemLongClickListener = { id, position ->
                    removePatient(id, position)
                }
            )
            binding.patientsRecyclerView.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = patientsAdapter
                isClickable = true

            }
            binding.constraintLayout.visibility = View.VISIBLE
            binding.ivEmptyList.visibility = View.GONE
        }
    }

    private fun removePatient(patientId: String, position: Int) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setMessage(R.string.patients_remove_dialgo_title)
            .setCancelable(false)
            .setPositiveButton("Sim") { dialog, _ ->
                viewModel.removePatient(patientId)
                patientsList.removeAt(position)
                patientsAdapter.notifyItemRemoved(position)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToPatientsDetails(patientId: String) {
        Toast.makeText(
            context,
            "Tela de detalhamento em construção",
            Toast.LENGTH_SHORT
        ).show()
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
