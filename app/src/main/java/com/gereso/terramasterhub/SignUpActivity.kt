package com.gereso.terramasterhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val firstNameEditText = findViewById<EditText>(R.id.textfirstname)
        val lastNameEditText = findViewById<EditText>(R.id.textlastname)
        val userNameEditText = findViewById<EditText>(R.id.textusername)
        val emailEditText = findViewById<EditText>(R.id.textemail)
        val passwordEditText = findViewById<EditText>(R.id.textpassword)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)

        progressBar.visibility = View.GONE

        signUpBtn.setOnClickListener {
            if (isValidInput(firstNameEditText) && isValidInput(lastNameEditText) &&
                isValidInput(userNameEditText) && isValidInput(emailEditText) &&
                isValidInput(passwordEditText)) {

                progressBar.visibility = View.VISIBLE

                val firstName = firstNameEditText.text.toString()
                val lastName = lastNameEditText.text.toString()
                val nickName = userNameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        progressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid

                            // Create a new user with a first and last name
                            val user = hashMapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "nickname" to nickName,
                                "email" to email
                            )

                            // Add a new document with a generated ID
                            if (userId != null) {
                                firestore.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(baseContext, "Account Created.", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(baseContext, "Error adding user: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun isValidInput(editText: EditText): Boolean {
        val text = editText.text.toString()
        if (text.isEmpty()) {
            editText.error = "This field is required"
            return false
        }
        return true
    }
}
