package com.sabal.terramasterhub.data.repositories
import android.content.Context
import android.util.Log
import com.sabal.terramasterhub.data.model.DeleteResponse
import com.sabal.terramasterhub.data.model.Expert
import com.sabal.terramasterhub.data.model.ExpertsResponse
import com.sabal.terramasterhub.data.model.Request
import com.sabal.terramasterhub.data.model.RequestResponse
import com.sabal.terramasterhub.data.model.RequestUpdate
import com.sabal.terramasterhub.data.model.UpdateResponse
import com.sabal.terramasterhub.data.network.MyApi

import com.sabal.terramasterhub.util.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinderRepository(private val context: Context) {
    private val myApi = MyApi.invoke()
    fun getAllRequests(callback: (List<Request>?, String?) -> Unit) {
        val token = PrefManager.getToken(context)
        Log.d("ExpertRepository", "Token: $token")

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            myApi.getAllFinderRequests(authToken).enqueue(object : Callback<RequestResponse> {
                override fun onResponse(call: Call<RequestResponse>, response: Response<RequestResponse>) {
                    if (response.isSuccessful) {
                        // Extract the list of experts from the response body
                        val requestsResponse = response.body()
                        if (requestsResponse != null) {
                            callback(requestsResponse.requests, null)
                        } else {
                            // Log the response if the body is null
                            Log.e("FinderRepository", "Response body is null")
                            callback(null, "Response body is null")
                        }
                    } else {
                        // Log the response code and error message
                        Log.e("FinderRepository", "Response failed with code: ${response.code()}")
                        callback(null, "Failed to fetch experts with code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<RequestResponse>, t: Throwable) {
                    // Log the error message
                    Log.e("FinderRepository", "Network error: ${t.message}")
                    callback(null, "Network error: ${t.message}")
                }
            })
        } else {
            callback(null, "Token is missing")
        }
    }

    fun updateRequest(id: String, updatedRequest: RequestUpdate, callback: (Boolean?, String?) -> Unit) {
        val token = PrefManager.getToken(context)

        if (token.isNullOrEmpty()) {
            callback(null, "Token is missing")
            return
        }

        val authToken = "Bearer $token"

        myApi.updateRequest(authToken, id, updatedRequest).enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                if (response.isSuccessful) {
                    val updateResponse = response.body()
                    if (updateResponse != null) {
                        callback(true, updateResponse.message) // Successfully updated
                    } else {
                        Log.e("FinderRepository", "Response body is null")
                        callback(null, "Update failed: Response body is null")
                    }
                } else {
                    Log.e("FinderRepository", "Response failed with code: ${response.code()} and message: ${response.message()}")
                    callback(null, "Failed to update request: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                Log.e("FinderRepository", "Network error: ${t.message}")
                callback(null, "Network error: ${t.message}")
            }
        })
    }


    fun deleteRequest(id: String, callback: (Boolean?, String?) -> Unit) {
        val token = PrefManager.getToken(context)

        if (token.isNullOrEmpty()) {
            callback(null, "Token is missing")
            return
        }
        val authToken = "Bearer $token"
        myApi.deleteRequest(authToken, id).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                if (response.isSuccessful) {
                    val deleteResponse = response.body()
                    if (deleteResponse != null) {
                        callback(true, deleteResponse.message)  // Request successfully deleted
                    } else {
                        Log.e("FinderRepository", "Delete failed: Response body is null")
                        callback(null, "Delete failed: Response body is null")
                    }
                } else {
                    Log.e("FinderRepository", "Response failed with code: ${response.code()} and message: ${response.message()}")
                    callback(null, "Failed to delete request: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Log.e("FinderRepository", "Network error: ${t.message}")
                callback(null, "Network error: ${t.message}")
            }
        })
    }
}