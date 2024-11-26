package com.sabal.terramasterhub.data.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.model.ConsultationRequest
import com.sabal.terramasterhub.data.repositories.ConsultationRepository
import com.sabal.terramasterhub.util.PrefManager


class Fragment6ViewModel (application: Application) : AndroidViewModel(application){

    private val repository: ConsultationRepository = ConsultationRepository(application)

    val expertRequests: MutableLiveData<List<ConsultationRequest>> = MutableLiveData()
    val surveyorRequests: MutableLiveData<List<ConsultationRequest>> = MutableLiveData()


    // Fetch requests based on the user type (expert or surveyor)
    fun fetchRequestsBasedOnUserType() {
        val userType = PrefManager.getUserType(getApplication()) // Get user type from PrefManager
        if (userType == "surveyor") {
            fetchSurveyorRequests()
        } else if (userType == "expert") {
            fetchExpertRequests()
        }
    }

    fun fetchExpertRequests() {
        repository.getExpertRequests { response ->
            response?.let {
                expertRequests.postValue(it.requests)
            }
        }
    }
    fun fetchSurveyorRequests() {
        repository.getSurveyorRequests { response ->
            response?.let {
                surveyorRequests.postValue(it.requests)
            }
        }
    }

    fun acceptRequest(request: ConsultationRequest) {
        repository.updateRequestStatus(request.id, "accepted") { message ->
            if (message != null) {
                Log.d("ViewModel", message)  // Log the success message
                fetchRequestsBasedOnUserType()  // Refresh data if successful
            } else {
                Log.e("ViewModel", "Failed to accept the request")
            }

        }
    }
    fun declineRequest(request: ConsultationRequest) {
        repository.updateRequestStatus(request.id, "declined") { message ->
            if (message != null) {
                Log.d("ViewModel", message)  // Log the success message
                fetchRequestsBasedOnUserType()  // Refresh data if successful
            } else {
                Log.e("ViewModel", "Failed to decline the request")
            }
        }
    }
}