package com.sabal.terramasterhub.data.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.data.repositories.UserRepository
import com.sabal.terramasterhub.data.view.AdminActivity
import com.sabal.terramasterhub.data.view.HomeActivity
import com.sabal.terramasterhub.data.view.ExpertSurveyorActivity
import com.sabal.terramasterhub.util.AuthListener
import com.sabal.terramasterhub.util.PrefManager
import org.json.JSONObject

class AuthViewModel(private val userRepository: UserRepository = UserRepository(MyApi())) : ViewModel() {

    // User input fields
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var user_type: String? = null
    var certification_id: String? = null
    var license_number: String? = null

    var authListener: AuthListener? = null

    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid email or password!", isRegister = false)
            return
        }

        val loginResponse = userRepository.userLogin(email!!, password!!)
        loginResponse.observeForever { response ->
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getString("message") == "Login Successful") {
                    val tokenType = jsonObject.getString("token_type")
                    val token = jsonObject.getString("token")
                    val fullToken = "$tokenType $token"

                    PrefManager.saveToken(view.context, fullToken)

                    // Extract user data from response
                    val user = jsonObject.getJSONObject("user")
                    val userType = user.getString("user_type") // Get the user type

                    // Navigate based on user type
                    val intent = when (userType) {
                        "admin" -> Intent(view.context, AdminActivity::class.java)
                        "surveyor", "expert" -> Intent(view.context, ExpertSurveyorActivity::class.java)
                        else -> Intent(view.context, HomeActivity::class.java)
                    }


                    view.context.startActivity(intent)
                    authListener?.onSuccess(loginResponse, isRegister = false)
                } else {
                    authListener?.onFailure("Login failed. Please try again.", isRegister = false)
                }
            } catch (e: Exception) {
                authListener?.onFailure("An error occurred: ${e.message}", isRegister = false)
            }
        }
    }
    // Handle register button click
    fun onRegisterButtonClick(view: View) {
        authListener?.onStarted()



        // Validate required fields
        if (name.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Please fill in all required fields correctly!", isRegister = true)
            return
        }

        // Validate user_type-specific fields
        if (user_type.isNullOrEmpty()) {
            authListener?.onFailure("Please select a valid user type!", isRegister = true)
            return
        }

        // Ensure only "finder" is allowed if not an expert or surveyor
        if (user_type?.lowercase() !in listOf("expert", "surveyor") && user_type?.lowercase() != "finder") {
            authListener?.onFailure("Invalid user type. Only 'finder' is allowed.", isRegister = true)
            return
        }

        when (user_type?.lowercase()) {
            "expert", "surveyor" -> {
                // Validate that expert and surveyor must provide certification_id and license_number
                if (certification_id.isNullOrEmpty() || license_number.isNullOrEmpty()) {
                    authListener?.onFailure("Experts and Surveyors must provide certification ID and license number!", isRegister = true)
                    return
                }
            }
            "finder" -> {
                // Reset optional fields for a "finder" user
                certification_id = "temp"
                license_number = "temp"
            }
            else -> {
                // Reset optional fields for non-expert and non-surveyor users
                certification_id = null
                license_number = null
            }
        }

        // Call the repository for registration
        val registerResponse = userRepository.userRegister(
            name!!, email!!, password!!, user_type!!, certification_id, license_number
        )

        registerResponse.observeForever { response ->
            if (response.contains("Registration successful", ignoreCase = true)) {
                authListener?.onSuccess(registerResponse, isRegister = true)
            } else {
                authListener?.onFailure(response, isRegister = true)
            }
        }
    }
}