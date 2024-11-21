package com.sabal.terramasterhub.data.repositories

import android.content.Context
import android.util.Log
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.ui.home.RequestUpdate
import com.sabal.terramasterhub.util.PrefManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultationRepository(private val api: MyApi) {

    fun updateRequest(
        context: Context,
        id: String,
        requestUpdate: RequestUpdate,
        callback: (Boolean) -> Unit
    ) {
        val authToken = PrefManager.getToken(context)

        if (authToken != null) {
            val bearerToken = "Bearer $authToken"

            api.updateRequest(bearerToken, id, requestUpdate).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Log.d("Request Update", "Request updated successfully")
                        callback(true) // Notify success
                    } else {
                        Log.e("Request Update", "Failed to update request. Response: ${response.message()}")
                        callback(false) // Notify failure
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Request Update", "Error: ${t.message}")
                    callback(false) // Notify failure
                }
            })
        } else {
            Log.e("Token Error", "Auth Token is null!")
            callback(false) // Notify failure due to missing token
        }
    }

    // In ConsultationRepository
    fun deleteRequest(
        context: Context,
        id: String,
        callback: (Boolean) -> Unit
    ) {
        val authToken = PrefManager.getToken(context)

        if (authToken != null) {
            val bearerToken = "Bearer $authToken"

            api.deleteRequest(bearerToken, id).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Log.d("Request Delete", "Request deleted successfully")
                        callback(true) // Notify success
                    } else {
                        Log.e("Request Delete", "Failed to delete request. Response: ${response.message()}")
                        callback(false) // Notify failure
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Request Delete", "Error: ${t.message}")
                    callback(false) // Notify failure
                }
            })
        } else {
            Log.e("Token Error", "Auth Token is null!")
            callback(false) // Notify failure due to missing token
        }
    }
}