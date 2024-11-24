package com.gereso.terramasterhub

import LoginRequest
import LoginResponse
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService // Declare the API service
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = RetrofitClient.apiService // Initialize your Retrofit API service
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

        val email = findViewById<EditText>(R.id.User)
        val password = findViewById<EditText>(R.id.Password)
        val signInButton = findViewById<Button>(R.id.signInBtn)

        signInButton.setOnClickListener {
            if (isValidInput(email) && isValidInput(password)) {
                progressBar.visibility = View.VISIBLE // Show progress bar during login attempt

                val loginRequest = LoginRequest(email.text.toString(), password.text.toString())
                apiService.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        progressBar.visibility = View.GONE // Hide progress bar once response is received
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            if (loginResponse != null && loginResponse.message == "Login Successful") {
                                // Save user data or token temporarily in SharedPreferences
                                saveLoginData(loginResponse)

                                Toast.makeText(baseContext, "Login Successful", Toast.LENGTH_SHORT).show()

                                // Redirect to HomePageActivity
                                val intent = Intent(this@LoginActivity, HomePageActivity::class.java)
                                startActivity(intent)
                                finish() // Close login activity
                            } else {
                                // Show error message from response if login fails
                                Toast.makeText(baseContext, "Login failed: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Handle unsuccessful response (e.g., server error)
                            Toast.makeText(baseContext, "Server error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE // Hide progress bar on failure
                        // Handle network failure
                        Toast.makeText(baseContext, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        val signUpTextView = findViewById<TextView>(R.id.signup)
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
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

    private fun saveLoginData(loginResponse: LoginResponse) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", loginResponse.token)
        editor.putString("user_id", loginResponse.user.id)
        editor.putString("user_name", loginResponse.user.name)
        editor.putString("user_email", loginResponse.user.email)
        editor.putString("user_type", loginResponse.user.user_type)
        editor.apply() // Save the changes
    }
}
