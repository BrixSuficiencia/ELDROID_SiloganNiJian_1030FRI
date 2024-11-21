package com.sabal.terramasterhub.ui

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
import com.sabal.terramasterhub.ui.home.Fragment3ViewModel
import com.sabal.terramasterhub.ui.home.Surveyor
import com.sabal.terramasterhub.ui.home.SurveyorAdapter

class Fragment3 : Fragment() {

    private lateinit var surveyorsRecyclerView: RecyclerView
    private lateinit var surveyorAdapter: SurveyorAdapter
    private lateinit var viewModel: Fragment3ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_3, container, false)

        // Initialize RecyclerView
        surveyorsRecyclerView = view.findViewById(R.id.recyclerViewSurveyors)
        surveyorsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize adapter with an empty list
        surveyorAdapter = SurveyorAdapter(requireContext(), listOf()) { surveyor ->
            // This is where we handle the consultation request logic
            showRequestMessageDialog(surveyor)
        }

        surveyorsRecyclerView.adapter = surveyorAdapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(Fragment3ViewModel::class.java)

        // Observe surveyors data from ViewModel
        viewModel.surveyors.observe(viewLifecycleOwner) { surveyors ->
            surveyorAdapter.updateSurveyors(surveyors)
        }

        // Observe error messages from ViewModel
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        // Fetch surveyors data from the API
        viewModel.fetchSurveyors()

        return view
    }
    private fun showRequestMessageDialog(surveyor: Surveyor) {
        // Inflate dialog layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_consultation_request, null)
        val messageInput: EditText = dialogView.findViewById(R.id.editTextMessage)

        // Create the AlertDialog and set the dialog view
        AlertDialog.Builder(requireContext())
            .setTitle("Request Consultation")
            .setView(dialogView)
            .setPositiveButton("Send") { _, _ ->
                val message = messageInput.text.toString().trim()
                if (message.isNotEmpty()) {
                    // Send consultation request via ViewModel
                    viewModel.sendConsultationRequest(surveyor.id, message)
                } else {
                    Toast.makeText(context, "Message cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
