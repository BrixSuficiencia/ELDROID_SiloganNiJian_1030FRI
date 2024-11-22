package com.gereso.terramasterhub

data class RegisterResponse(
    val success: Boolean,  // Indicates if the registration was successful
    val message: String    // Message from the backend (e.g., error message or success message)
)
