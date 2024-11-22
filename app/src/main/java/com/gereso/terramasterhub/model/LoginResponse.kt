data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String,
    val user: User
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val user_type: String
)
