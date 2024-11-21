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
import com.sabal.terramasterhub.ui.home.Expert
import com.sabal.terramasterhub.ui.home.ExpertAdapter
import com.sabal.terramasterhub.ui.home.Fragment2ViewModel

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
            // This is where we handle the consultation request logic
            showRequestMessageDialog(expert)
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

        // Fetch experts
        viewModel.fetchExperts()

        return view
    }

    private fun showRequestMessageDialog(expert: Expert) {
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
                    viewModel.sendConsultationRequest(expert.id, message)
                } else {
                    Toast.makeText(context, "Message cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}
