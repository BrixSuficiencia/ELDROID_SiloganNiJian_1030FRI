package com.sabal.terramasterhub.data.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.view.adapter.UserAdminAdapter
import com.sabal.terramasterhub.data.model.User
import com.sabal.terramasterhub.data.viewmodel.Fragment9ViewModel

class Fragment9 : Fragment() {

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var userAdminAdapter: UserAdminAdapter
    private lateinit var viewModel: Fragment9ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = inflater.inflate(R.layout.fragment_9, container, false)

        // Initialize the RecyclerView
        usersRecyclerView = binding.findViewById(R.id.usersRecyclerView)
        usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter with an empty list initially
        userAdminAdapter = UserAdminAdapter(requireContext(), listOf(), onDeleteClick = { user ->
            showDeleteConfirmationDialog(user)
        })
        usersRecyclerView.adapter = userAdminAdapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(Fragment9ViewModel::class.java)

        // Observe user data from ViewModel
        viewModel.users.observe(viewLifecycleOwner) { users ->
            userAdminAdapter.updateUsers(users)
        }
        // Observe the success message for user deletion
        viewModel.deleteUserSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "User deleted successfully", Toast.LENGTH_SHORT).show()
            }
        }
        // Observe error message in case of failure
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        }

        // Fetch users from the ViewModel
        viewModel.getAllUsers()

        return binding
    }

    private fun showDeleteConfirmationDialog(user: User) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Are you sure you want to delete ${user.name}?")
            .setMessage("This action cannot be undone.")
            .setPositiveButton("Yes") { _, _ ->
                // Perform deletion through ViewModel
                viewModel.deleteUser(user.id)

            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }
}
