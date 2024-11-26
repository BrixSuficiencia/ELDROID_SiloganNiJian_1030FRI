package com.sabal.terramasterhub.data.repositories

import android.content.Context
import android.util.Log
import com.sabal.terramasterhub.data.model.ConsultationAcceptDeclineResponse
import com.sabal.terramasterhub.data.model.ConsultationLog
import com.sabal.terramasterhub.data.model.ConsultationLogResponse
import com.sabal.terramasterhub.data.model.ConsultationRequestsResponse

import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.util.PrefManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultationRepository(private val context: Context) {

    private val myApi = MyApi.invoke()

    // Function to fetch expert consultation requests
    fun getExpertRequests(onResult: (ConsultationRequestsResponse?) -> Unit) {
        val token = PrefManager.getToken(context)

        myApi.getExpertRequests("Bearer $token").enqueue(object : Callback<ConsultationRequestsResponse> {
            override fun onResponse(
                call: Call<ConsultationRequestsResponse>,
                response: Response<ConsultationRequestsResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    Log.e("ConsultationRepository", "Error: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ConsultationRequestsResponse>, t: Throwable) {
                Log.e("ConsultationRepository", "Network Error: ${t.message}")
                onResult(null)
            }
        })
    }

    // Function to fetch surveyor consultation requests
    fun getSurveyorRequests(onResult: (ConsultationRequestsResponse?) -> Unit) {
        val token = PrefManager.getToken(context)

        myApi.getSurveyorRequests("Bearer $token").enqueue(object : Callback<ConsultationRequestsResponse> {
            override fun onResponse(
                call: Call<ConsultationRequestsResponse>,
                response: Response<ConsultationRequestsResponse>
            ) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    Log.e("ConsultationRepository", "Error: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ConsultationRequestsResponse>, t: Throwable) {
                Log.e("ConsultationRepository", "Network Error: ${t.message}")
                onResult(null)
            }
        })
    }

    fun updateRequestStatus(requestId: String, status: String, onResult: (String?) -> Unit) {
        val token = PrefManager.getToken(context)
        val call: Call<ConsultationAcceptDeclineResponse> = when (status) {
            "accepted" -> myApi.acceptConsultationRequest(requestId, "Bearer $token")
            "declined" -> myApi.declineConsultationRequest(requestId, "Bearer $token")
            else -> {
                onResult(null)
                return
            }
        }

        call.enqueue(object : Callback<ConsultationAcceptDeclineResponse> {
            override fun onResponse(call: Call<ConsultationAcceptDeclineResponse>, response: Response<ConsultationAcceptDeclineResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body()?.message)  // Pass the success message to the ViewModel
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ConsultationRepository", "Error: $errorBody")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ConsultationAcceptDeclineResponse>, t: Throwable) {
                Log.e("ConsultationRepository", "Network Error: ${t.message}")
                onResult(null)
            }
        })
    }

    fun getConsultationLogs(onResult: (List<ConsultationLog>?) -> Unit) {
        val token = PrefManager.getToken(context)

        myApi.getLogs("Bearer $token").enqueue(object : Callback<ConsultationLogResponse> {
            override fun onResponse(
                call: Call<ConsultationLogResponse>,
                response: Response<ConsultationLogResponse>
            ) {
                if (response.isSuccessful) {
                    // Extract the list of consultation logs and pass it to onResult
                    onResult(response.body()?.consultation_logs)
                } else {
                    Log.e("ConsultationRepository", "Error: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ConsultationLogResponse>, t: Throwable) {
                Log.e("ConsultationRepository", "Network Error: ${t.message}")
                onResult(null)
            }
        })
    }
}