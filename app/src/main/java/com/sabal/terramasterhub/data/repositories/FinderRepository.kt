package com.sabal.terramasterhub.data.repositories
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.ui.home.ConsultationRequest
import com.sabal.terramasterhub.ui.home.Request
import com.sabal.terramasterhub.ui.home.RequestsResponse
import com.sabal.terramasterhub.util.PrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinderRepository(private val context: Context) {

    private val api: MyApi = MyApi.invoke()

    fun getAllRequests(callback: (List<Request>?, String?) -> Unit) {
        val token = PrefManager.getToken(context)
        Log.d("RequestRepository", "Token: $token")

        if (token != null) {
            // Add the 'Bearer' keyword followed by the token
            val authToken = "Bearer $token"

            api.getAllRequests(authToken).enqueue(object : Callback<RequestsResponse> {
                override fun onResponse(call: Call<RequestsResponse>, response: Response<RequestsResponse>) {
                    if (response.isSuccessful) {
                        // Extract the list of requests from the response body
                        val requestsResponse = response.body()
                        if (requestsResponse != null) {
                            callback(requestsResponse.requests, null)
                        } else {
                            // Log the response if the body is null
                            Log.e("RequestRepository", "Response body is null")
                            callback(null, "Response body is null")
                        }
                    } else {
                        // Log the response code and error message
                        Log.e("RequestRepository", "Response failed with code: ${response.code()}")
                        callback(null, "Failed to fetch requests with code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<RequestsResponse>, t: Throwable) {
                    // Log the error message
                    Log.e("RequestRepository", "Network error: ${t.message}")
                    callback(null, "Network error: ${t.message}")
                }
            })
        } else {
            callback(null, "Token is missing")
        }
    }


}