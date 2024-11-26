package com.sabal.terramasterhub.data.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.model.Update
import com.sabal.terramasterhub.data.view.adapter.UpdateAdapter
import com.sabal.terramasterhub.data.view.adapter.UpdateAdminAdapter
import com.sabal.terramasterhub.data.viewmodel.Fragment5ViewModel
import com.sabal.terramasterhub.data.viewmodel.Fragment7ViewModel


class Fragment7 : Fragment() {
    private lateinit var updatesRecyclerView: RecyclerView
    private lateinit var viewModel: Fragment7ViewModel
    private lateinit var updateAdminAdapter: UpdateAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_7, container, false)

        updatesRecyclerView = view.findViewById(R.id.recyclerViewUpdates)
        updatesRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize UpdateAdminAdapter with empty list and callback functions
        updateAdminAdapter = UpdateAdminAdapter(
            requireContext(),
            listOf(),
            onUpdateClick = { update ->
                // Handle the Update button click
                showEditUpdateDialog(update)
            },
            onDeleteClick = { update ->
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("Are you sure?")
                    .setMessage("Do you really want to delete the update titled: '${update.title}'?")
                    .setCancelable(true)
                    .setPositiveButton("Yes") { _, _ ->
                        // If "Yes" is clicked, proceed with deletion
                        Toast.makeText(context, "Deleting: ${update.title}", Toast.LENGTH_SHORT).show()
                        viewModel.deleteUpdate(update.id)
                    }
                    .setNegativeButton("No") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .create()

                // Show the dialog
                dialog.show()
            }
        )

        updatesRecyclerView.adapter = updateAdminAdapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(Fragment7ViewModel::class.java)

        // Observe updates data from ViewModel
        viewModel.dashboardData.observe(viewLifecycleOwner) { updates ->
            updateAdminAdapter.updateUpdates(updates)
        }

        // Observe error state
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        // Fetch updates from ViewModel
        viewModel.fetchAdminDashboard()

        // Handle Add Update button click
        val buttonAddUpdate: Button = view.findViewById(R.id.buttonAddUpdate)
        buttonAddUpdate.setOnClickListener {
            showAddUpdateDialog()
        }

        return view
    }
    private fun showAddUpdateDialog() {
        // Inflate the dialog layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_update, null)

        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTitle)
        val contentEditText: EditText = dialogView.findViewById(R.id.editTextContent)
        val submitButton: Button = dialogView.findViewById(R.id.buttonSubmitUpdate)

        // Create the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add New Update")
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Set onClickListener for Submit button
        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotBlank() && content.isNotBlank()) {
                // Pass data to ViewModel
                viewModel.addUpdate(title, content)
                Toast.makeText(context, "Update added successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                // Show error message if fields are empty
                Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }
        // Show the dialog
        dialog.show()
    }

    private fun showEditUpdateDialog(update: Update) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_update, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTitle)
        val contentEditText: EditText = dialogView.findViewById(R.id.editTextContent)
        val submitButton: Button = dialogView.findViewById(R.id.buttonSubmitUpdate)

        // Set current data to EditText fields
        titleEditText.setText(update.title)
        contentEditText.setText(update.content)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Update")
            .setView(dialogView)
            .setCancelable(true)
            .create()

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotBlank() && content.isNotBlank()) {
                // Call the ViewModel to update the update
                viewModel.editUpdate(update.id, title, content)
                Toast.makeText(context, "Update edited successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
