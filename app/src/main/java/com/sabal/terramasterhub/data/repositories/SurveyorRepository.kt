package com.sabal.terramasterhub.data.repositories


import com.sabal.terramasterhub.ui.home.SurveyorsResponse
import android.content.Context
import android.util.Log
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.ui.home.ConsultationRequest
import com.sabal.terramasterhub.ui.home.ConsultationRequestSurveyor
import com.sabal.terramasterhub.ui.home.Surveyor
import com.sabal.terramasterhub.util.PrefManager
import okhttp3.ResponseBody
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

    fun requestConsultation(surveyorId: String, message: String, callback: (Boolean, String?) -> Unit) {
        // Get the token from PrefManager (or wherever it's stored)
        val token = PrefManager.getToken(context)

        // Log expertId if it's not null
        if (surveyorId.isNotEmpty()) {
            Log.d("ConsultationRequest", "Surveyor ID: $surveyorId")
        }

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            // Create the ConsultationRequest object
            val consultationRequest = ConsultationRequestSurveyor(surveyorId, message)

            // Make the API call and handle success/error
            myApi.sendConsultationRequestSurveyor(consultationRequest, authToken).enqueue(object : Callback<ResponseBody> {
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
