package com.sabal.terramasterhub.data.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.Request
import com.sabal.terramasterhub.data.model.RequestUpdate
import com.sabal.terramasterhub.data.view.adapter.ExpertAdapter
import com.sabal.terramasterhub.data.view.adapter.RequestAdapter
import com.sabal.terramasterhub.data.viewmodel.Fragment2ViewModel
import com.sabal.terramasterhub.data.viewmodel.Fragment4ViewModel


class Fragment4 : Fragment() {

    private lateinit var requestsRecyclerView: RecyclerView
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var viewModel: Fragment4ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_4, container, false)

        requestsRecyclerView = view.findViewById(R.id.recyclerViewRequests)
        requestsRecyclerView.layoutManager = LinearLayoutManager(context)

        requestAdapter = RequestAdapter(
            requireContext(),
            listOf(),
            onRequestConsultation = { request ->
                // Handle the consultation request logic when the user clicks "Update"
                showRequestConsultationDialog(request)
            },
            onDeleteRequest = { request ->
                showDeleteConfirmationDialog(request)  // Add this function call
            }
        )

        // Set the adapter to RecyclerView
        requestsRecyclerView.adapter = requestAdapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(Fragment4ViewModel::class.java)

        // Observe data from ViewModel
        viewModel.requests.observe(viewLifecycleOwner) { requests ->
            // Update the adapter with the new list of requests
            requestAdapter.updateRequests(requests)
        }

        // Observe error messages from ViewModel
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        // Observe success state for consultation request
        viewModel.success.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Consultation request sent successfully!", Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch the requests from ViewModel
        viewModel.fetchRequests()

        return view
    }
    // Show a dialog or new screen for consultation (placeholder function)
    private fun showRequestConsultationDialog(request: Request) {
        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Request")

        // Inflate a custom layout for input fields
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_request, null)
        builder.setView(dialogView)

        // Get references to dialog input fields
        val messageEditText = dialogView.findViewById<EditText>(R.id.editTextMessage)
        val dateEditText = dialogView.findViewById<EditText>(R.id.editTextDate)
        val timeEditText = dialogView.findViewById<EditText>(R.id.editTextTime)
        val locationEditText = dialogView.findViewById<EditText>(R.id.editTextLocation)
        val rateEditText = dialogView.findViewById<EditText>(R.id.editTextRate)

        // Pre-fill fields with existing data
        messageEditText.setText(request.message)
        dateEditText.setText(request.date)
        timeEditText.setText(request.time)
        locationEditText.setText(request.location)
        rateEditText.setText(request.rate.toString())

        builder.setPositiveButton("Update") { _, _ ->

            val updatedRequest = RequestUpdate(
                message = messageEditText.text.toString(),
                date = dateEditText.text.toString(),
                time = timeEditText.text.toString(),
                location = locationEditText.text.toString(),
                rate = rateEditText.text.toString().toDouble() // Parse the rate as Double
            )
            // Call ViewModel to update the request
            viewModel.updateRequest(request.id, updatedRequest)
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun showDeleteConfirmationDialog(request: Request) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Request")
            .setMessage("Are you sure you want to delete this request?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteRequest(request.id)  // Call ViewModel's delete function
            }
            .setNegativeButton("No", null)
            .show()
    }
}