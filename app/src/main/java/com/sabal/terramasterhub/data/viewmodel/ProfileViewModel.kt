package com.sabal.terramasterhub.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.repositories.UserRepository
import com.sabal.terramasterhub.data.model.ProfileUpdateRequest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getProfile(context: Context): LiveData<Map<String, String>> {
        return userRepository.fetchUserProfile(context)
    }
    fun updateProfile(context: Context, name: String, email: String, password: String): LiveData<Boolean> {
        val updateResult = MutableLiveData<Boolean>()

        viewModelScope.launch {
            val profileUpdateRequest = ProfileUpdateRequest(
                name = name,
                email = email,
                password = password,
                password_confirmation = password
            )

            // Use callback to handle success/failure
            userRepository.updateProfile(context, profileUpdateRequest) { success ->
                updateResult.postValue(success) // Notify observers
            }
        }

        return updateResult
    }
}