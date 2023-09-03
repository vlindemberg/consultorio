package com.marcelo.piscologo.consultorio.presentation.patient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marcelo.piscologo.consultorio.data.model.Patient
import com.marcelo.piscologo.consultorio.databinding.PatientListItemBinding

class PatientsAdapter(
    private var patientList: List<Patient>,
    private val onItemClickListener: (patient: Patient) -> Unit
) : RecyclerView.Adapter<PatientsAdapter.PatientViewHolder>() {

    class PatientViewHolder(val binding: PatientListItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun setFilteredList(patientsList: List<Patient>) {
        this.patientList = patientsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = PatientListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patientList[position]
        holder.binding.apply {
            name.text = patient.name
            this.root.setOnClickListener { onItemClickListener.invoke(patient) }
        }

    }

    override fun getItemCount(): Int = patientList.size
}