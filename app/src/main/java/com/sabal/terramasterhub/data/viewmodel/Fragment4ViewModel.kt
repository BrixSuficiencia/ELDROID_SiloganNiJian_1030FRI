package com.sabal.terramasterhub.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.model.Request
import com.sabal.terramasterhub.data.model.RequestUpdate
import com.sabal.terramasterhub.data.repositories.FinderRepository


class Fragment4ViewModel(application: Application) : AndroidViewModel(application) {

    private val finderRepository: FinderRepository = FinderRepository(application)
    private val _requests = MutableLiveData<List<Request>>()
    val requests: LiveData<List<Request>> get() = _requests
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Success LiveData for consultation request
    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success
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
    // New: Update request method
    fun updateRequest(id: String, updatedRequest: RequestUpdate) {

        finderRepository.updateRequest(id, updatedRequest) { success, error ->
            if (success != null && success) {
                _success.value = true
                fetchRequests()  // Refresh the request list
            } else {
                _error.value = error
            }
        }
    }

    fun deleteRequest(id: String) {
        finderRepository.deleteRequest(id) { success, error ->
            if (success == true) {
                _success.value = true
                fetchRequests()  // Refresh the request list after deletion
            } else {
                _error.value = error
            }
        }
    }
}
