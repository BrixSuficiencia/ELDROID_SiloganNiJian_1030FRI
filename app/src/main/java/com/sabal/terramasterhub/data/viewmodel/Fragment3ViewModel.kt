package com.sabal.terramasterhub.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.repositories.SurveyorRepository
import com.sabal.terramasterhub.data.model.Surveyor

class Fragment3ViewModel(application: Application) : AndroidViewModel(application) {

    private val surveyorRepository: SurveyorRepository = SurveyorRepository(application)
    private val _surveyors = MutableLiveData<List<Surveyor>>()
    val surveyors: LiveData<List<Surveyor>> get() = _surveyors
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Success LiveData for consultation request
    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success


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

    fun sendConsultationRequestSurveyor(
        surveyor_id: String,
        message: String,
        date: String,
        time: String,
        location: String,
        rate: Double
    ) {
        surveyorRepository.sendConsultationRequestSurveyor(surveyor_id, message, date, time, location, rate) { success, error ->
            if (success) {
                _success.value = true
            } else {
                _error.value = error
            }
        }
    }
}
