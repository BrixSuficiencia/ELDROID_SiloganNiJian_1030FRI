package com.sabal.terramasterhub.data.repositories


import com.sabal.terramasterhub.data.model.SurveyorsResponse
import android.content.Context
import android.util.Log
import com.sabal.terramasterhub.data.model.ConsultationRequestExpert
import com.sabal.terramasterhub.data.model.ConsultationRequestSurveyor
import com.sabal.terramasterhub.data.model.ConsultationResponse
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.data.model.Surveyor
import com.sabal.terramasterhub.util.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyorRepository(private val context: Context) {

    private val myApi = MyApi.invoke()

    fun getSurveyorsFromApi(callback: (List<Surveyor>?, String?) -> Unit) {
        val token = PrefManager.getToken(context)
        Log.d("SurveyorRepository", "Token: $token")

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            myApi.getSurveyors(authToken).enqueue(object : Callback<SurveyorsResponse> {
                override fun onResponse(call: Call<SurveyorsResponse>, response: Response<SurveyorsResponse>) {
                    if (response.isSuccessful) {
                        // Extract the list of surveyors from the response body
                        val surveyorsResponse = response.body()
                        if (surveyorsResponse != null) {
                            callback(surveyorsResponse.surveyors, null)
                        } else {
                            // Log the response if the body is null
                            Log.e("SurveyorRepository", "Response body is null")
                            callback(null, "Response body is null")
                        }
                    } else {
                        // Log the response code and error message
                        Log.e("SurveyorRepository", "Response failed with code: ${response.code()}")
                        callback(null, "Failed to fetch surveyors with code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<SurveyorsResponse>, t: Throwable) {
                    // Log the error message
                    Log.e("SurveyorRepository", "Network error: ${t.message}")
                    callback(null, "Network error: ${t.message}")
                }
            })
        } else {
            callback(null, "Token is missing")
        }
    }
    // New method to send a consultation request
    fun sendConsultationRequestSurveyor(
        surveyor_id: String,
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
            val consultationRequest = ConsultationRequestSurveyor(
                surveyor_id = surveyor_id,
                message = message,
                date = date,
                time = time,
                location = location,
                rate = rate
            )

            myApi.sendConsultationRequestSurveyor(authToken, consultationRequest).enqueue(object : Callback<ConsultationResponse> {
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
