package com.sabal.terramasterhub.data.repositories

import android.content.Context
import android.util.Log
import com.sabal.terramasterhub.data.model.User
import com.sabal.terramasterhub.data.model.UserResponse
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.util.PrefManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminRepository (private val context: Context){

    private val myApi = MyApi.invoke()
    fun getAllUsers(callback: (List<User>?, String?) -> Unit) {
        val token = PrefManager.getToken(context)
        Log.d("UserRepository", "Token: $token")

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            myApi.getAllUsers(authToken).enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        // Extract the list of users from the response body
                        val userResponse = response.body()
                        if (userResponse != null) {
                            callback(userResponse.users, null) // Pass users list to the callback
                        } else {
                            // Log if the response body is null
                            Log.e("AdminRepository", "Response body is null")
                            callback(null, "Response body is null") // Return error message
                        }
                    } else {
                        // Log the response code and error message
                        Log.e("AdminRepository", "Response failed with code: ${response.code()}")
                        callback(null, "Failed to fetch users with code: ${response.code()}") // Return error message
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    // Log the error message
                    Log.e("AdminRepository", "Network error: ${t.message}")
                    callback(null, "Network error: ${t.message}") // Return error message
                }
            })
        } else {
            callback(null, "Token is missing") // Return error message if token is missing
        }
    }

    fun deleteUser(userId: String, callback: (Boolean, String?) -> Unit) {
        val token = PrefManager.getToken(context)
        Log.d("AdminRepository", "Token: $token")

        if (token != null) {
            val authToken = "Bearer $token"

            // Call the deleteUser endpoint
            myApi.deleteUser(userId, authToken).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        callback(true, null)  // User deletion was successful
                    } else {
                        Log.e("AdminRepository", "Delete failed with code: ${response.code()}")
                        callback(false, "Failed to delete user: ${response.message()}")  // Pass error message
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("AdminRepository", "Network error: ${t.message}")
                    callback(false, "Network error: ${t.message}")  // Pass error message
                }
            })
        } else {
            callback(false, "Token is missing")  // Token not available
        }
    }
}