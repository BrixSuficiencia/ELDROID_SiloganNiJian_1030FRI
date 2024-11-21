package com.sabal.terramasterhub.ui.auth

data class ProfileUpdateRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String
)