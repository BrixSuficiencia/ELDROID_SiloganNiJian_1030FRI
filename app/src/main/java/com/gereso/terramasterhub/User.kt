package com.gereso.terramasterhub

data class User(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val profileImageUrl: String = ""
) {
    val name: String
        get() = "$firstName $lastName"
}
