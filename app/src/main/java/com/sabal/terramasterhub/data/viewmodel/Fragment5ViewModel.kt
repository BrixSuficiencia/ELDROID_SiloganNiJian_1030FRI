package com.sabal.terramasterhub.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.model.Update
import com.sabal.terramasterhub.data.repositories.DashboardRepository

class Fragment5ViewModel(application: Application) : AndroidViewModel(application) {

    private val dashboardRepository: DashboardRepository = DashboardRepository(application)
    // LiveData to observe dashboard updates
    private val _dashboardData = MutableLiveData<List<Update>>()
    val dashboardData: LiveData<List<Update>> get() = _dashboardData

    // LiveData to observe error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Function to fetch dashboard data
    fun fetchDashboard() {
        dashboardRepository.getDashboard { updates, errorMessage ->
            if (updates != null) {
                // Successfully fetched dashboard data, update the LiveData
                _dashboardData.value = updates
            } else {
                // If there was an error, update the error LiveData
                _error.value = errorMessage ?: "Unknown error"
            }
        }
    }

}