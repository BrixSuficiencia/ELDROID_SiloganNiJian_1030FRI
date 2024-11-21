package com.sabal.terramasterhub.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sabal.terramasterhub.data.network.MyApi
import com.sabal.terramasterhub.data.repositories.ConsultationRepository
import com.sabal.terramasterhub.data.repositories.FinderRepository
import com.sabal.terramasterhub.data.repositories.SurveyorRepository
import kotlinx.coroutines.launch

class Fragment4ViewModel(application: Application) : AndroidViewModel(application) {

    // Initialize the repository for handling API calls
    private val finderRepository: FinderRepository = FinderRepository(application)
    private val consultationRepository: ConsultationRepository = ConsultationRepository(MyApi())

    // LiveData for storing the list of requests
    private val _requests = MutableLiveData<List<Request>>()
    val requests: LiveData<List<Request>> get() = _requests
    // LiveData for error or success messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // LiveData for update status
    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> get() = _updateStatus

    // Method to fetch all requests from the API
    fun fetchRequests() {
        finderRepository.getAllRequests { requests, error ->
            if (requests != null) {
                _requests.value = requests
            }
            if (error != null) {
                _error.value = error
            }
        }
    }
    // Update request method
    fun updateRequest(context: Application, id: String, requestUpdate: RequestUpdate) {
        viewModelScope.launch {
            consultationRepository.updateRequest(context, id, requestUpdate) { success ->
                _updateStatus.postValue(success) // Notify observers of success or failure
                if (success) {
                    // Fetch the updated requests from the server after update
                    fetchRequests()
                }
            }
        }
    }

    // In Fragment4ViewModel
    fun deleteRequest(context: Application, id: String) {
        viewModelScope.launch {
            consultationRepository.deleteRequest(context, id) { success ->
                if (success) {
                    // Refresh the requests after deleting
                    fetchRequests()
                } else {
                    _error.value = "Failed to delete request"
                }
            }
        }
    }


}
