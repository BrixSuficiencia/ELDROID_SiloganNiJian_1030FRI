package com.sabal.terramasterhub.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.model.Update
import com.sabal.terramasterhub.data.repositories.DashboardRepository

class Fragment7ViewModel(application: Application) : AndroidViewModel(application) {

    private val dashboardRepository: DashboardRepository = DashboardRepository(application)
    // LiveData to observe dashboard updates
    private val _dashboardData = MutableLiveData<List<Update>>()
    val dashboardData: LiveData<List<Update>> get() = _dashboardData

    // LiveData to observe error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

        fun fetchAdminDashboard() {
            dashboardRepository.getAdminDashboard { updates, errorMessage ->
                if (updates != null) {
                    // Successfully fetched dashboard data, update the LiveData
                    _dashboardData.value = updates
                } else {
                    // If there was an error, update the error LiveData
                    _error.value = errorMessage ?: "Unknown error"
                }
            }
        }
        fun addUpdate(title: String, content: String) {
            // Create a new Update object (you can also add other fields such as the current time for `created_at`)
            val newUpdate = Update(
                title = title,
                content = content,
                created_at = "temp",
                updated_at = "temp",
                id = "temp"
            )

            // Pass the update to the repository to save it
            dashboardRepository.addUpdate(newUpdate) { success, errorMessage ->
                if (success) {
                    // Fetch updated dashboard data
                    fetchAdminDashboard()
                } else {
                    // Handle the error if the update was not added
                    _error.value = errorMessage ?: "Failed to add update"
                }
            }
        }
        fun editUpdate(updateId: String, title: String, content: String) {
            // Create the Update object with the new title and content
            val update = Update(id = updateId, title = title, content = content, created_at = "temp", updated_at = "temp")

            // Call the repository function to edit the update
            dashboardRepository.editUpdate(updateId, update) { success, errorMessage ->
                if (success) {
                    fetchAdminDashboard()
                } else {
                    _error.value = errorMessage
            }
        }

    }

        fun deleteUpdate(updateId: String) {
        dashboardRepository.deleteUpdate(updateId) { success, errorMessage ->
            if (success) {
                // Successfully deleted the update, fetch the updated dashboard
                fetchAdminDashboard()
            } else {
                // Handle the error if the update could not be deleted
                _error.value = errorMessage ?: "Failed to delete update"
            }
        }
    }
}