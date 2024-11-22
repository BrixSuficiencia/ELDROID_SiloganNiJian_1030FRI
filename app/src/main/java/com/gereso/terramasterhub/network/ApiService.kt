package com.gereso.terramasterhub

import ConsultationRequest
import ForgotPasswordRequest
import LoginRequest
import LoginResponse
import ResetPasswordRequest
import Update
import UpdateProfileRequest
import UpdateRequest
import UserProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

interface ApiService {

    // Auth Routes
    @POST("api/register")  // Adjust the endpoint path based on your backend
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("api/login")  // Login user
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("forgot-password")  // Forgot password
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<Map<String, String>>

    @POST("reset-password")  // Reset password
    fun resetPassword(@Body request: ResetPasswordRequest): Call<Map<String, String>>

    // User Routes (Requires Auth)
    @POST("logout")  // Logout user
    fun logoutUser(): Call<Map<String, String>>

    @GET("profile")  // Get user profile
    fun getProfile(): Call<UserProfileResponse>

    @PUT("profile/update")  // Update user profile
    fun updateProfile(@Body request: UpdateProfileRequest): Call<Map<String, String>>

    // Consultation Routes (Requires Auth)
    @GET("consultation/getAllRequests")  // Get all consultation requests
    fun getAllConsultationRequests(): Call<List<ConsultationRequest>>

    @POST("request-consultation/expert")  // Request an expert consultation
    fun requestExpertConsultation(@Body request: ConsultationRequest): Call<Map<String, String>>

    @POST("request-consultation/surveyor")  // Request a surveyor consultation
    fun requestSurveyorConsultation(@Body request: ConsultationRequest): Call<Map<String, String>>

    @PUT("consultation/updateRequest/{id}")  // Update consultation request
    fun updateConsultationRequest(@Path("id") id: Int, @Body request: ConsultationRequest): Call<Map<String, String>>

    @DELETE("consultation/deleteRequest/{id}")  // Delete consultation request
    fun deleteConsultationRequest(@Path("id") id: Int): Call<Map<String, String>>

    @GET("consultation/requests/expert")  // Get expert consultation requests
    fun getExpertConsultationRequests(): Call<List<ConsultationRequest>>

    @GET("consultation/requests/surveyor")  // Get surveyor consultation requests
    fun getSurveyorConsultationRequests(): Call<List<ConsultationRequest>>

    @POST("consultation/accept/{id}")  // Accept a consultation request (expert or surveyor)
    fun acceptConsultationRequest(@Path("id") id: Int): Call<Map<String, String>>

    @POST("consultation/decline/{id}")  // Decline a consultation request (expert or surveyor)
    fun declineConsultationRequest(@Path("id") id: Int): Call<Map<String, String>>

    // Admin Routes (Requires Admin Auth)
    @GET("admin/getAllUsers")  // Get all users (Admin)
    fun getAllUsers(): Call<List<User>>

    @GET("admin/dashboard")  // Admin dashboard
    fun getAdminDashboard(): Call<Map<String, String>>

    @GET("admin/getAllUpdates")  // Get all updates (Admin)
    fun getAllUpdates(): Call<List<Update>>

    @POST("admin/postUpdate")  // Post a new update (Admin)
    fun postUpdate(@Body request: UpdateRequest): Call<Map<String, String>>

    @PUT("admin/editUpdate/{id}")  // Edit an existing update (Admin)
    fun editUpdate(@Path("id") id: Int, @Body request: UpdateRequest): Call<Map<String, String>>

    @DELETE("admin/deleteUpdate/{id}")  // Delete an update (Admin)
    fun deleteUpdate(@Path("id") id: Int): Call<Map<String, String>>

    @DELETE("admin/deleteUser/{id}")  // Delete or terminate a user (Admin)
    fun terminateUser(@Path("id") id: Int): Call<Map<String, String>>
}
