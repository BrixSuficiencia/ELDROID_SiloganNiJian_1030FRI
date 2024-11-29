package com.sabal.terramasterhub.data.view.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.Expert
import com.sabal.terramasterhub.data.view.adapter.ExpertAdapter
import com.sabal.terramasterhub.data.viewmodel.Fragment2ViewModel

class Fragment2 : Fragment() {

    private lateinit var expertsRecyclerView: RecyclerView
    private lateinit var expertAdapter: ExpertAdapter
    private lateinit var viewModel: Fragment2ViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_2, container, false)

        expertsRecyclerView = view.findViewById(R.id.recyclerViewExperts)
        expertsRecyclerView.layoutManager = LinearLayoutManager(context)

        expertAdapter = ExpertAdapter(requireContext(), listOf()) { expert ->
            // Handle the consultation request logic
            showRequestConsultationDialog(expert)
        }

        expertsRecyclerView.adapter = expertAdapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(Fragment2ViewModel::class.java)

        // Observe data from ViewModel
        viewModel.experts.observe(viewLifecycleOwner) { experts ->
            expertAdapter.updateExperts(experts)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        // Observe success state for consultation request
        viewModel.success.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Consultation request sent successfully!", Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch experts
        viewModel.fetchExperts()
        return view
    }

    // Function to show the consultation request dialog
    @SuppressLint("MissingInflatedId")
    private fun showRequestConsultationDialog(expert: Expert) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_request_consultation, null)

        // Initialize dialog input fields
        val messageEditText = dialogView.findViewById<EditText>(R.id.editTextMessage)
        val dateEditText = dialogView.findViewById<EditText>(R.id.editTextDate)
        val timeEditText = dialogView.findViewById<EditText>(R.id.editTextTime)
        val locationEditText = dialogView.findViewById<EditText>(R.id.editTextLocation)
        val rateEditText = dialogView.findViewById<EditText>(R.id.editTextRate)

        // Show the AlertDialog
        AlertDialog.Builder(context, R.style.CustomAlertDialogTheme)
            .setView(dialogView)
            .setPositiveButton("Submit") { _, _ ->
                val message = messageEditText.text.toString().trim()
                val date = dateEditText.text.toString().trim()
                val time = timeEditText.text.toString().trim()
                val location = locationEditText.text.toString().trim()
                val rate = rateEditText.text.toString().toDoubleOrNull() ?: -1.0

                // Basic validation
                if (message.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty() || rate <= 0) {
                    Toast.makeText(context, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show()
                } else {
                    // Send consultation request via ViewModel
                    viewModel.sendConsultationRequestExpert(
                        expert.id, message, date, time, location, rate
                    )
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}