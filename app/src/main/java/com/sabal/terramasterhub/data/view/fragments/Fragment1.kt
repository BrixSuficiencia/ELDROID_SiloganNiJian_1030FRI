package com.sabal.terramasterhub.data.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AlertDialog
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.data.repositories.UserRepository
import com.sabal.terramasterhub.data.view.LoginActivity
import com.sabal.terramasterhub.data.viewmodel.ProfileViewModel
import com.sabal.terramasterhub.data.viewmodel.ProfileViewModelFactory
import com.sabal.terramasterhub.util.PrefManager

class Fragment1 : Fragment(R.layout.fragment_1) {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView: TextView = view.findViewById(R.id.textName)
        val emailTextView: TextView = view.findViewById(R.id.textEmail)
        val userTypeTextView: TextView = view.findViewById(R.id.textUserType)

        // Initialize the ViewModel
        profileViewModel = ViewModelProvider(
            this, ProfileViewModelFactory(UserRepository(MyApi()))
        ).get(ProfileViewModel::class.java)

        // Observe the profile data
        profileViewModel.getProfile(requireContext()).observe(viewLifecycleOwner, Observer { profileData ->
            // Log the profile data
            Log.d("ProfileData", "Fetched profile data: $profileData")

            if (profileData.isNotEmpty()) {
                nameTextView.text = profileData["name"]
                emailTextView.text = profileData["email"]
                userTypeTextView.text = profileData["user_type"]
            } else {
                Toast.makeText(requireContext(), "Failed to fetch profile data.", Toast.LENGTH_SHORT).show()
            }
        })

        // Button to edit profile
        val btnEditProfile: Button = view.findViewById(R.id.btnEditProfile)
        btnEditProfile.setOnClickListener {
            showEditProfileDialog(nameTextView.text.toString(), emailTextView.text.toString())
        }

        val btnLogout: ImageView = view.findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun showEditProfileDialog(currentName: String, currentEmail: String) {
        // Create input fields for the dialog
        val editTextName = EditText(requireContext())
        val editTextEmail = EditText(requireContext())
        val editTextPassword = EditText(requireContext())
        val editTextPasswordConfirm = EditText(requireContext())

        // Pre-fill the fields with the current profile data
        editTextName.setText(currentName)
        editTextEmail.setText(currentEmail)

        // Create a container layout (LinearLayout)
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL // Stack the EditTexts vertically

        // Add the EditTexts to the layout
        layout.addView(editTextName)
        layout.addView(editTextEmail)
        layout.addView(editTextPassword)
        layout.addView(editTextPasswordConfirm)

        // Create the AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(layout) // Set the layout with all EditTexts
            .setPositiveButton("Save") { _, _ ->
                // Get the updated values from the EditTexts
                val updatedName = editTextName.text.toString()
                val updatedEmail = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                val passwordConfirm = editTextPasswordConfirm.text.toString()

                // Validate password and confirmation match
                if (password != passwordConfirm) {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Pass data to ViewModel for further processing
                profileViewModel.updateProfile(requireContext(), updatedName, updatedEmail, password)
                    .observe(viewLifecycleOwner, Observer { updateSuccess ->
                        if (updateSuccess) {
                            // Refresh the profile data on successful update
                            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()

                            // Use safe calls to avoid null pointer exception
                            profileViewModel.getProfile(requireContext()).observe(viewLifecycleOwner, Observer { profileData ->
                                // Ensure the view is non-null before calling findViewById
                                view?.findViewById<TextView>(R.id.textName)?.text = profileData["name"]
                                view?.findViewById<TextView>(R.id.textEmail)?.text = profileData["email"]
                            })
                        } else {
                            Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
                        }
                    })

            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun logout() {
        // Clear all stored preferences, including token and user info
        PrefManager.clear(requireContext())

        // Log the logout action for debugging
        Log.d("Logout", "User logged out. All preferences cleared.")

        // Navigate to the login screen
        requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })

        // Show a toast confirmation
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}
