package com.sabal.terramasterhub.data.model

data class ProfileUpdateRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String
)