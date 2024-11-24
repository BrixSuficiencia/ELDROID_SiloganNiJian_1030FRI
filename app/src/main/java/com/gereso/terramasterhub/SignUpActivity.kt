package com.gereso.terramasterhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        apiService = RetrofitClient.apiService

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val nameEditText = findViewById<EditText>(R.id.textname)
        val userTypeEditText = findViewById<EditText>(R.id.textusertype)
        val emailEditText = findViewById<EditText>(R.id.textemail)
        val passwordEditText = findViewById<EditText>(R.id.textpassword)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)

        progressBar.visibility = View.GONE

        signUpBtn.setOnClickListener {
            if (isValidInput(nameEditText) && isValidInput(userTypeEditText) &&
                isValidInput(emailEditText) && isValidInput(passwordEditText)) {

                progressBar.visibility = View.VISIBLE

                // Retrieve values from input fields
                val name = nameEditText.text.toString()
                val user_type = userTypeEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                // Add static values for certification_id, license_number, and pricing
                val certification_id = "temp" // Temporary certification ID
                val license_number = "temp" // Temporary license number
                // val pricing = 0 // Default pricing value

                // Create registration request object
                val registerRequest = RegisterRequest(
                    name = name,
                    email = email,
                    password = password,
                    user_type = user_type,
                    certification_id = certification_id,
                    license_number = license_number
                    // pricing = pricing
                )

                // Send API request for user registration
                apiService.registerUser(registerRequest).enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        progressBar.visibility = View.GONE
                        if (response.isSuccessful) {
                            val registerResponse = response.body()
                            if (registerResponse != null && registerResponse.success) {
                                Toast.makeText(baseContext, "Account Created. Redirecting to Login...", Toast.LENGTH_SHORT).show()

                                // Redirect to LoginActivity
                                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish() // Prevent returning to the Sign-Up screen
                            } else {
                                val errorMessage = registerResponse?.message ?: "Registration failed. Please try again."
                                Toast.makeText(baseContext, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val errorResponse = response.message()
                            Toast.makeText(baseContext, "Server error: $errorResponse", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(baseContext, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
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
