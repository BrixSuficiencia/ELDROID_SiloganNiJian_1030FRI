package com.sabal.terramasterhub.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.repositories.ExpertRepository

class Fragment2ViewModel(application: Application) : AndroidViewModel(application) {

    private val expertRepository: ExpertRepository = ExpertRepository(application)
    private val _experts = MutableLiveData<List<Expert>>()
    val experts: LiveData<List<Expert>> get() = _experts
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Fetch experts list from the repository
    fun fetchExperts() {
        expertRepository.getExpertsFromApi { experts, error ->
            if (experts != null) {
                _experts.value = experts
            }
            if (error != null) {
                _error.value = error
            }
        }
    }

    // Send consultation request to the repository
    fun sendConsultationRequest(expertId: String, message: String) {
        expertRepository.requestConsultation(expertId, message) { success, error ->
            if (success) {
                // Notify the UI about success
                _error.postValue("Request sent successfully!")
            } else {
                // Notify the UI about the error
                _error.postValue(error ?: "Unknown error occurred")
            }
        }
    }
}
