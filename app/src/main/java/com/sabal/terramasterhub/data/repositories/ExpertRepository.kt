package com.sabal.terramasterhub.data.repositories

import android.content.Context
import android.util.Log
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.ui.home.ConsultationRequest
import com.sabal.terramasterhub.ui.home.Expert
import com.sabal.terramasterhub.ui.home.ExpertsResponse
import com.sabal.terramasterhub.util.PrefManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExpertRepository(private val context: Context) {

    private val myApi = MyApi.invoke()

    fun getExpertsFromApi(callback: (List<Expert>?, String?) -> Unit) {
        val token = PrefManager.getToken(context)
        Log.d("ExpertRepository", "Token: $token")

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            myApi.getExperts(authToken).enqueue(object : Callback<ExpertsResponse> {
                override fun onResponse(call: Call<ExpertsResponse>, response: Response<ExpertsResponse>) {
                    if (response.isSuccessful) {
                        // Extract the list of experts from the response body
                        val expertsResponse = response.body()
                        if (expertsResponse != null) {
                            callback(expertsResponse.experts, null)
                        } else {
                            // Log the response if the body is null
                            Log.e("ExpertRepository", "Response body is null")
                            callback(null, "Response body is null")
                        }
                    } else {
                        // Log the response code and error message
                        Log.e("ExpertRepository", "Response failed with code: ${response.code()}")
                        callback(null, "Failed to fetch experts with code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ExpertsResponse>, t: Throwable) {
                    // Log the error message
                    Log.e("ExpertRepository", "Network error: ${t.message}")
                    callback(null, "Network error: ${t.message}")
                }
            })
        } else {
            callback(null, "Token is missing")
        }
    }
    fun requestConsultation(expertId: String, message: String, callback: (Boolean, String?) -> Unit) {
        // Get the token from PrefManager (or wherever it's stored)
        val token = PrefManager.getToken(context)

        // Log expertId if it's not null
        if (expertId.isNotEmpty()) {
            Log.d("ConsultationRequest", "Expert ID: $expertId")
        }

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            // Create the ConsultationRequest object
            val consultationRequest = ConsultationRequest(expertId, message)

            // Make the API call and handle success/error
            myApi.sendConsultationRequestExpert(consultationRequest, authToken).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        callback(true, null)
                    } else {
                        callback(false, "Failed to send request")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback(false, t.message)
                }
            })
        } else {
            callback(false, "Token is missing")
        }
    }
}
