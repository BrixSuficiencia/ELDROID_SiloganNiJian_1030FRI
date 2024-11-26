package com.sabal.terramasterhub.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.model.ConsultationLog
import com.sabal.terramasterhub.data.model.ConsultationLogResponse
import com.sabal.terramasterhub.data.repositories.ConsultationRepository

class Fragment8ViewModel (application: Application) : AndroidViewModel(application) {

    private val consultationRepository = ConsultationRepository(application)

    // LiveData to hold the list of consultation logs
    val consultationLogs = MutableLiveData<List<ConsultationLog>>()

    // Function to fetch consultation logs
    fun getConsultationLogs() {
        // Use the repository to fetch the data
        consultationRepository.getConsultationLogs { logs ->
            // Pass the result (a List<ConsultationLog>) to the LiveData object
            consultationLogs.value = logs
        }
    }
}