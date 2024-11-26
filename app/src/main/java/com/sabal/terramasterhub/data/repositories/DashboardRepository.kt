package com.sabal.terramasterhub.data.repositories

import android.content.Context
import android.util.Log
import com.sabal.terramasterhub.data.model.DashboardResponse
import com.sabal.terramasterhub.data.model.Update
import com.sabal.terramasterhub.data.model.UpdatePostResponse
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.util.PrefManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardRepository(private val context: Context) {

    private val myApi = MyApi.invoke()
    fun getDashboard(callback: (List<Update>?, String?) -> Unit) {
        val token = PrefManager.getToken(context)
        Log.d("DashboardRepository", "Token: $token")

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            myApi.getDashboard(authToken).enqueue(object : Callback<DashboardResponse> {
                override fun onResponse(
                    call: Call<DashboardResponse>,
                    response: Response<DashboardResponse>
                ) {
                    if (response.isSuccessful) {
                        // Extract the updates from the response body
                        val dashboardResponse = response.body()
                        if (dashboardResponse != null) {
                            callback(dashboardResponse.updates, null)
                        } else {
                            // Log the response if the body is null
                            Log.e("DashboardRepository", "Response body is null")
                            callback(null, "Response body is null")
                        }
                    } else {
                        // Log the response code and error message
                        Log.e(
                            "DashboardRepository",
                            "Response failed with code: ${response.code()}"
                        )
                        callback(null, "Failed to fetch dashboard with code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                    // Log the error message
                    Log.e("DashboardRepository", "Network error: ${t.message}")
                    callback(null, "Network error: ${t.message}")
                }
            })
        } else {
            callback(null, "Token is missing")
        }
    }

    // Function to get Admin Dashboard data
    fun getAdminDashboard(callback: (List<Update>?, String?) -> Unit) {
        val token = PrefManager.getToken(context)
        Log.d("DashboardRepository", "Token: $token")

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            myApi.getAdminDashboard(authToken).enqueue(object : Callback<DashboardResponse> {
                override fun onResponse(
                    call: Call<DashboardResponse>,
                    response: Response<DashboardResponse>
                ) {
                    if (response.isSuccessful) {
                        // Extract the updates from the response body
                        val dashboardResponse = response.body()
                        if (dashboardResponse != null) {
                            callback(dashboardResponse.updates, null)
                        } else {
                            // Log the response if the body is null
                            Log.e("DashboardRepository", "Response body is null")
                            callback(null, "Response body is null")
                        }
                    } else {
                        // Log the response code and error message
                        Log.e(
                            "DashboardRepository",
                            "Response failed with code: ${response.code()}"
                        )
                        callback(null, "Failed to fetch dashboard with code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                    // Log the error message
                    Log.e("DashboardRepository", "Network error: ${t.message}")
                    callback(null, "Network error: ${t.message}")
                }
            })
        } else {
            callback(null, "Token is missing")
        }
    }

    fun addUpdate(update: Update, callback: (Boolean, String?) -> Unit) {
        val token = PrefManager.getToken(context)
        Log.d("DashboardRepository", "Token: $token")

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            // Call the POST method in MyApi and send the update object
            myApi.postAdminUpdate(authToken, update).enqueue(object : Callback<UpdatePostResponse> {
                override fun onResponse(
                    call: Call<UpdatePostResponse>,
                    response: Response<UpdatePostResponse>
                ) {
                    if (response.isSuccessful) {
                        // Successfully added the update
                        callback(true, null)
                    } else {
                        // Handle failure (response not successful)
                        Log.e(
                            "DashboardRepository",
                            "Failed to add update with code: ${response.code()}"
                        )
                        callback(false, "Failed to add update with code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UpdatePostResponse>, t: Throwable) {
                    // Handle network error
                    Log.e("DashboardRepository", "Network error: ${t.message}")
                    callback(false, "Network error: ${t.message}")
                }
            })
        } else {
            callback(false, "Token is missing")
        }
    }

    fun editUpdate(id: String, update: Update, callback: (Boolean, String?) -> Unit) {
        val token = PrefManager.getToken(context)
        Log.d("DashboardRepository", "Token: $token")

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            // Call the PUT method in MyApi with the 'id' in the path
            myApi.editAdminUpdate(id, authToken, update)
                .enqueue(object : Callback<UpdatePostResponse> {
                    override fun onResponse(
                        call: Call<UpdatePostResponse>,
                        response: Response<UpdatePostResponse>
                    ) {
                        if (response.isSuccessful) {
                            // Successfully edited the update
                            callback(true, null)
                        } else {
                            // Handle failure (response not successful)
                            Log.e(
                                "DashboardRepository",
                                "Failed to edit update with code: ${response.code()}"
                            )
                            callback(false, "Failed to edit update with code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<UpdatePostResponse>, t: Throwable) {
                        // Handle network error
                        Log.e("DashboardRepository", "Network error: ${t.message}")
                        callback(false, "Network error: ${t.message}")
                    }
                })
        } else {
            callback(false, "Token is missing")
        }
    }
    fun deleteUpdate(updateId: String, callback: (Boolean, String?) -> Unit) {
        val token = PrefManager.getToken(context) // Fetch the token (this assumes you're using a token for authentication)

        if (token != null) {
            val authToken = "Bearer $token" // Add the Bearer prefix

            // Call the API to delete the update
            myApi.deleteAdminUpdate(updateId, authToken).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        callback(true, null) // Success
                    } else {
                        callback(false, "Failed to delete update with code: ${response.code()}") // Failure
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback(false, "Network error: ${t.message}") // Failure
                }
            })
        } else {
            callback(false, "Token is missing") // Token is missing
        }
    }
}