package com.gereso.terramasterhub

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val user_type: String,
    val certification_id: String,
    val license_number: String,
    // val pricing: Int
)


