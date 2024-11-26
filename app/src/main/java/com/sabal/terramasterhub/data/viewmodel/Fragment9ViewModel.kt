package com.sabal.terramasterhub.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabal.terramasterhub.data.model.User
import com.sabal.terramasterhub.data.repositories.AdminRepository
import com.sabal.terramasterhub.data.repositories.UserRepository

class Fragment9ViewModel (application: Application) : AndroidViewModel(application) {
    private val adminRepository = AdminRepository(application)

    // LiveData to observe the users data
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _deleteUserSuccess = MutableLiveData<Boolean>()
    val deleteUserSuccess: LiveData<Boolean> get() = _deleteUserSuccess

    // Function to fetch all users
    fun getAllUsers() {
        adminRepository.getAllUsers { usersList, error ->
            if (usersList != null) {
                // Update LiveData with the fetched users
                _users.value = usersList
            } else {
                // Update LiveData with the error message
                _errorMessage.value = error
            }
        }
    }

    // Function to delete a user
    fun deleteUser(userId: String) {
        adminRepository.deleteUser(userId) { success, errorMessage ->
            if (success) {
                // If deletion is successful, update LiveData
                _deleteUserSuccess.value = true
                getAllUsers()
            } else {
                // If deletion fails, update error message LiveData
                _errorMessage.value = errorMessage
            }
        }
    }
}