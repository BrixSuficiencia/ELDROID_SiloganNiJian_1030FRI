package com.gereso.terramasterhub

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditUserActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var currentUser: FirebaseUser? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var imageView: ImageView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var emailEditText: EditText

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        progressBar = findViewById(R.id.progressBar)
        imageView = findViewById(R.id.imageView6)
        firstNameEditText = findViewById(R.id.textfirstname)
        lastNameEditText = findViewById(R.id.textlastname)
        userNameEditText = findViewById(R.id.textusername)
        emailEditText = findViewById(R.id.textemail)
        val updateBtn: Button = findViewById(R.id.updateBtn)

        // Fetch current user
        currentUser = auth.currentUser

        // Populate EditText fields with user's current information
        populateUserInfo()

        // Set click listener for the image view to pick an image from gallery
        imageView.setOnClickListener {
            pickImageFromGallery()
        }

        // Set click listener for the update button
        updateBtn.setOnClickListener {
            updateUserInfo()
        }
    }

    private fun populateUserInfo() {
        // Retrieve user's current information from Firestore and populate EditText fields
        val userId = currentUser?.uid
        userId?.let { uid ->
            val docRef = db.collection("users").document(uid)
            docRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val firstName = document.getString("firstName") ?: ""
                    val lastName = document.getString("lastName") ?: ""
                    val username = document.getString("nickname") ?: ""
                    val email = document.getString("email") ?: ""

                    firstNameEditText.setText(firstName)
                    lastNameEditText.setText(lastName)
                    userNameEditText.setText(username)
                    emailEditText.setText(email)

                    // Retrieve image URL
                    val imageUrl = document.getString("image")
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this).load(imageUrl).into(imageView)
                    }
                }
            }.addOnFailureListener { exception ->
                // Handle any errors
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            // Retrieve selected image URI
            selectedImageUri = result.data?.data
            // Load selected image into the image view using Glide or any other image loading library
            Glide.with(this).load(selectedImageUri).into(imageView)
        }
    }

    private fun updateUserInfo() {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()
        val username = userNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()

        // Validate input fields
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Show progress bar
        progressBar.visibility = View.VISIBLE

        // Update user information in Firestore
        val userId = currentUser?.uid
        userId?.let { uid ->
            val userRef = db.collection("users").document(uid)

            // Ensure userInfo is of type MutableMap<String, Any>
            val userInfo: MutableMap<String, Any> = hashMapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "nickname" to username,
                "email" to email
            )

            userRef.update(userInfo)
                .addOnSuccessListener {
                    // Update successful
                    Toast.makeText(this, "User information updated", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    finish()
                }
                .addOnFailureListener { e ->
                    // Update failed
                    Toast.makeText(this, "Failed to update user information: ${e.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }

            // Upload selected image to Firebase Storage if a new image was selected
            if (selectedImageUri != null) {
                val imageRef = storage.reference.child("profile_images/$uid.jpg")
                imageRef.putFile(selectedImageUri!!)
                    .addOnSuccessListener { _ ->
                        // Image upload successful
                        // Update the user document with the new image URL
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            userRef.update("image", uri.toString())
                                .addOnSuccessListener {
                                    // Image URL update successful
                                    Toast.makeText(this, "User image updated", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    // Image URL update failed
                                    Toast.makeText(this, "Failed to update user image URL: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        // Image upload failed
                        Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // No new image selected, finish the activity
                finish()
            }
        }
    }
}
