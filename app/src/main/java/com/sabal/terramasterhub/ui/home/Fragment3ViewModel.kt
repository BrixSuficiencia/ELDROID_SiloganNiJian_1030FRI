package com.sabal.terramasterhub.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.repositories.SurveyorRepository

class Fragment3ViewModel(application: Application) : AndroidViewModel(application) {

    private val surveyorRepository: SurveyorRepository = SurveyorRepository(application)
    private val _surveyors = MutableLiveData<List<Surveyor>>()
    val surveyors: LiveData<List<Surveyor>> get() = _surveyors
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Fetch surveyors list from the repository
    fun fetchSurveyors() {
        surveyorRepository.getSurveyorsFromApi { surveyors, error ->
            if (surveyors != null) {
                _surveyors.value = surveyors
            }
            if (error != null) {
                _error.value = error
            }
        }
    }
    // Send consultation request to the repository
    fun sendConsultationRequest(surveyorId: String, message: String) {
        surveyorRepository.requestConsultation(surveyorId, message) { success, error ->
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
