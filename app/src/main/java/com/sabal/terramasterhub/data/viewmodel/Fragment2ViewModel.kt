package com.sabal.terramasterhub.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.repositories.ExpertRepository
import com.sabal.terramasterhub.data.model.Expert

class Fragment2ViewModel(application: Application) : AndroidViewModel(application) {

    private val expertRepository: ExpertRepository = ExpertRepository(application)
    private val _experts = MutableLiveData<List<Expert>>()
    val experts: LiveData<List<Expert>> get() = _experts
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Success LiveData for consultation request
    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

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

    // Send a consultation request
    fun sendConsultationRequestExpert(
        expert_id: String,
        message: String,
        date: String,
        time: String,
        location: String,
        rate: Double
    ) {
        expertRepository.sendConsultationRequestExpert(expert_id, message, date, time, location, rate) { success, error ->
            if (success) {
                _success.value = true
            } else {
                _error.value = error
            }
        }
    }
}