package com.sabal.terramasterhub.ui

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.ui.home.Fragment4ViewModel
import com.sabal.terramasterhub.ui.home.Request
import com.sabal.terramasterhub.ui.home.RequestAdapter
import com.sabal.terramasterhub.ui.home.RequestUpdate

class Fragment4 : Fragment() {

    private lateinit var requestsRecyclerView: RecyclerView
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var viewModel: Fragment4ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_4, container, false)

        // Initialize RecyclerView
        requestsRecyclerView = view.findViewById(R.id.recyclerViewRequests)
        requestsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(Fragment4ViewModel::class.java)

        // Inside Fragment4
        requestAdapter = RequestAdapter(listOf(), { request ->
            // Handle request click here
            showRequestMessageDialog(request)
        }, { request ->
            // Handle delete click here
            showDeleteConfirmationDialog(request)
        })

        // Set adapter to RecyclerView
        requestsRecyclerView.adapter = requestAdapter

        // Observe the requests LiveData from the ViewModel
        viewModel.requests.observe(viewLifecycleOwner, Observer { requests ->
            // Update the RecyclerView with the new list of requests
            requestAdapter.updateRequests(requests)
        })

        // Observe error messages from the ViewModel
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            // Show error message if necessary
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        })

        // Observe update status to show success/failure message
        viewModel.updateStatus.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(context, "Request updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to update request", Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch initial requests
        viewModel.fetchRequests()

        return view
    }

    private fun showRequestMessageDialog(request: Request) {
        // Create an EditText to allow user input for the updated message
        val editText = EditText(requireContext()).apply {
            setText(request.message) // Pre-fill with the current message
        }

        val builder = AlertDialog.Builder(requireContext())

        // Set dialog title and message
        builder.setTitle("Request Details")
        builder.setMessage("Message: ${request.message}\n" +
                "Status: ${request.status}\n" +
                "Updated At: ${request.updated_at}\n" +
                "Expert: ${request.expert?.name ?: "Not assigned"}\n" +
                "Surveyor: ${request.surveyor?.name ?: "Not assigned"}")

        // Add the EditText to the dialog layout
        builder.setView(editText)

        // Set up the "Update" button
        builder.setPositiveButton("Update") { dialog, which ->
            // Get the updated message from the EditText
            val updatedMessage = editText.text.toString()

            // Check if the message is not empty before updating
            if (updatedMessage.isNotBlank()) {
                // Create a RequestUpdate object with the updated message
                val requestUpdate = RequestUpdate(updatedMessage)

                // Log the request.id and requestUpdate to check their values
                Log.d("RequestUpdate", "Request ID: ${request.id}")
                Log.d("RequestUpdate", "Updated Message: $updatedMessage")

                // Optionally, display the values using Toast for quick feedback
                Toast.makeText(requireContext(), "Request ID: ${request.id}, Updated Message: $updatedMessage", Toast.LENGTH_LONG).show()

                // Call the ViewModel to update the request
                viewModel.updateRequest(requireContext().applicationContext as Application, request.id, requestUpdate)
            } else {
                // Optionally, show an error message if the user didn't input a message
                Toast.makeText(requireContext(), "Message cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the "Cancel" button to dismiss the dialog
        builder.setNeutralButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        // Show the dialog
        builder.show()
    }
    private fun showDeleteConfirmationDialog(request: Request) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Request")
            .setMessage("Are you sure you want to delete this request?")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteRequest(requireContext().applicationContext as Application, request.id)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        builder.show()
    }

}
