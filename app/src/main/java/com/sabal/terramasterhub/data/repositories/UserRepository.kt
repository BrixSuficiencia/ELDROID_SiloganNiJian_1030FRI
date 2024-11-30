package com.sabal.terramasterhub.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.data.model.ProfileUpdateRequest
import com.sabal.terramasterhub.util.PrefManager
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

    class UserRepository(private val api: MyApi) {

        fun userLogin(email: String, password: String): LiveData<String> {
        val loginResponse = MutableLiveData<String>()

            api.userLogin(email, password).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    loginResponse.value = response.body()?.string()
                    Log.d("Login Success", "Login successful! Response: ${response.body()?.string()}")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    loginResponse.value = errorMessage
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle network failure or exception
                loginResponse.value = t.message ?: "Network error"
            }
        })

        return loginResponse
    }

    // Function to register the user
    fun userRegister(
        name: String,
        email: String,
        password: String,
        user_type: String?,
        certification_id: String?,
        license_number: String?,
    ): LiveData<String> {
        val registerResponse = MutableLiveData<String>()

        api.userRegister(name, email, password, user_type ?: ""
            , certification_id ?: "",
            license_number ?: "")
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // Successfully registered
                        registerResponse.value = "Registration successful!"
                    } else {
                        // Handle error response
                        registerResponse.value = response.errorBody()?.string() ?: "Unknown error"
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle network failure or exception
                    registerResponse.value = t.message ?: "Network error"
                }
            })

        return registerResponse
    }
    fun fetchUserProfile(context: Context): LiveData<Map<String, String>> {
        val profileResponse = MutableLiveData<Map<String, String>>()

        val authToken = PrefManager.getToken(context)

        if (authToken != null) {
            // Log the token being passed
            Log.d("TOKEN_DEBUG", "Auth Token: $authToken")

            // Adding Bearer token to the Authorization header
            val bearerToken = "Bearer $authToken"

            api.getProfile(bearerToken).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    // Read the response body only once and store it
                    val responseString = response.body()?.string()

                    // Log the raw API response
                    Log.d("API_RESPONSE_DEBUG", "Raw Response: $responseString")

                    if (response.isSuccessful && responseString != null) {
                        try {
                            // Parse the JSON response only once
                            val jsonObject = JSONObject(responseString)
                            // Log parsed JSON object
                            Log.d("API_PARSED_DEBUG", "Parsed JSON: $jsonObject")

                            if (jsonObject.getString("message") == "Profile Fetched.") {
                                val data = jsonObject.getJSONObject("data")
                                val profileData = mapOf(
                                    "name" to data.getString("name"),
                                    "email" to data.getString("email"),
                                    "user_type" to data.getString("user_type"),
                                    "updated_at" to data.getString("updated_at"),
                                    "created_at" to data.getString("created_at"),
                                    "id" to data.getString("id")
                                )
                                profileResponse.value = profileData
                            } else {
                                Log.d("API_ERROR", "Unexpected message: ${jsonObject.getString("message")}")
                                profileResponse.value = emptyMap()
                            }
                        } catch (e: Exception) {
                            Log.e("API_PARSING_ERROR", "Error parsing JSON: ${e.message}")
                            profileResponse.value = emptyMap()
                        }
                    } else {
                        Log.e("API_ERROR", "Unsuccessful response: ${response.code()} - ${response.message()}")
                        profileResponse.value = emptyMap()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("API_CALL_FAILURE", "Failed to call API: ${t.message}")
                    profileResponse.value = emptyMap()
                }
            })
        } else {
            Log.e("TOKEN_ERROR", "Auth Token is null!")
            profileResponse.value = emptyMap()
        }

        return profileResponse
    }

    fun updateProfile(context: Context, profileUpdateRequest: ProfileUpdateRequest, callback: (Boolean) -> Unit) {
        val authToken = PrefManager.getToken(context)

        if (authToken != null) {
            val bearerToken = "Bearer $authToken"

            api.updateProfile(bearerToken, profileUpdateRequest).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Log.d("Profile Update", "Profile updated successfully")
                        callback(true) // Notify success
                    } else {
                        Log.e("Profile Update", "Failed to update profile. Response: ${response.message()}")
                        callback(false) // Notify failure
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Profile Update", "Error: ${t.message}")
                    callback(false) // Notify failure
                }
            })
        } else {
            Log.e("Token Error", "Auth Token is null!")
            callback(false) // Notify failure due to missing token
        }
    }
}


