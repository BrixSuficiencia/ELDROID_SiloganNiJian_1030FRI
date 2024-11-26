package com.sabal.terramasterhub.data.repositories

import android.content.Context
import android.util.Log
import com.sabal.terramasterhub.data.model.ConsultationRequestExpert
import com.sabal.terramasterhub.data.model.ConsultationResponse

import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.data.model.Expert
import com.sabal.terramasterhub.data.model.ExpertsResponse
import com.sabal.terramasterhub.util.PrefManager

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


    // New method to send a consultation request
    fun sendConsultationRequestExpert(
        expert_id: String,
        message: String,
        date: String,
        time: String,
        location: String,
        rate: Double,
        callback: (Boolean, String?) -> Unit
    ) {
        val token = PrefManager.getToken(context)
        if (token != null) {
            val authToken = "Bearer $token"

            // Assuming MyApi has a method for sending consultation requests
            val consultationRequest = ConsultationRequestExpert(
                expert_id = expert_id,
                message = message,
                date = date,
                time = time,
                location = location,
                rate = rate
            )

            myApi.sendConsultationRequestExpert(authToken, consultationRequest).enqueue(object : Callback<ConsultationResponse> {
                override fun onResponse(call: Call<ConsultationResponse>, response: Response<ConsultationResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        callback(true, null)
                    } else {
                        callback(false, response.body()?.message ?: "Failed to send consultation request")
                    }
                }

                override fun onFailure(call: Call<ConsultationResponse>, t: Throwable) {
                    callback(false, "Network error: ${t.message}")
                }
            })
        } else {
            callback(false, "Token is missing")
        }
    }


}
