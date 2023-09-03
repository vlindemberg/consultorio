package com.marcelo.piscologo.consultorio.presentation.patient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.marcelo.piscologo.consultorio.databinding.FragmentNewPatientBinding

class NewPatientFragment : Fragment() {

    private lateinit var binding: FragmentNewPatientBinding
    private val viewModel: PatientViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPatientBinding.inflate(layoutInflater)
        return binding.root
    }
}