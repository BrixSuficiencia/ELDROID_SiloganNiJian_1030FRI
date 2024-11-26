package com.sabal.terramasterhub.util

import androidx.lifecycle.LiveData

interface AuthListener {
    // Progress bar when the operation is starting
    fun onStarted()

    // When the auth operation is successful
    fun onSuccess(response: LiveData<String>, isRegister: Boolean)

    // When the auth operation fails
    fun onFailure(message: String, isRegister: Boolean)
}
