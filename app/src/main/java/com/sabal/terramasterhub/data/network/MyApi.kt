package com.sabal.terramasterhub.data.network


import com.sabal.terramasterhub.data.model.ConsultationAcceptDeclineResponse
import com.sabal.terramasterhub.data.model.ConsultationLogResponse
import com.sabal.terramasterhub.data.model.ConsultationRequestExpert
import com.sabal.terramasterhub.data.model.ConsultationRequestSurveyor
import com.sabal.terramasterhub.data.model.ConsultationRequestsResponse
import com.sabal.terramasterhub.data.model.ConsultationResponse
import com.sabal.terramasterhub.data.model.DashboardResponse
import com.sabal.terramasterhub.data.model.DeleteResponse
import com.sabal.terramasterhub.data.model.ProfileUpdateRequest
import com.sabal.terramasterhub.data.model.ExpertsResponse
import com.sabal.terramasterhub.data.model.RequestResponse
import com.sabal.terramasterhub.data.model.RequestUpdate
import com.sabal.terramasterhub.data.model.SurveyorsResponse
import com.sabal.terramasterhub.data.model.Update
import com.sabal.terramasterhub.data.model.UpdatePostResponse
import com.sabal.terramasterhub.data.model.UpdateResponse
import com.sabal.terramasterhub.data.model.UserResponse
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MyApi {

    @FormUrlEncoded
        @POST("login")
        fun userLogin(
    @Field("email") email: String,
    @Field("password") password: String
    ) : Call<ResponseBody>
    @FormUrlEncoded

    @POST("register")
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("user_type") user_type: String,
        @Field("certification_id") certification_id: String,
        @Field("license_number") license_number:String
    ) : Call<ResponseBody>
    @GET("profile")
    fun getProfile(@Header("Authorization") authToken: String): Call<ResponseBody>
    @PUT("profile/update")
    fun updateProfile(
        @Header("Authorization") authToken: String,
        @Body profileUpdateRequest: ProfileUpdateRequest
    ): Call<ResponseBody>
    @GET("experts")
    fun getExperts(@Header("Authorization") authToken: String): Call<ExpertsResponse>
    @GET("surveyors")
    fun getSurveyors(@Header("Authorization") authToken: String): Call<SurveyorsResponse>


    @POST("request-consultation/expert")
    fun sendConsultationRequestExpert(
        @Header("Authorization") token: String,
        @Body consultationRequestExpert: ConsultationRequestExpert
    ): Call<ConsultationResponse>

    @POST("request-consultation/surveyor")
    fun sendConsultationRequestSurveyor(
        @Header("Authorization") token: String,
        @Body consultationRequestSurveyor: ConsultationRequestSurveyor
    ): Call<ConsultationResponse>

    @GET("consultation/getAllRequests")
    fun getAllFinderRequests(
        @Header("Authorization") token: String
    ): Call<RequestResponse>

    @PUT("consultation/updateRequest/{id}")
    fun updateRequest(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body updatedRequest: RequestUpdate  // Add this line for the request body
    ): Call<UpdateResponse>

    @DELETE("consultation/deleteRequest/{id}")
    fun deleteRequest(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DeleteResponse>  // Define DeleteResponse below

    @GET("dashboard")
    fun getDashboard(
        @Header("Authorization") token: String
    ): Call<DashboardResponse>


    //FOR EXPERTS
    @GET("consultation/requests/expert")
    fun getExpertRequests(
    @Header("Authorization") token: String):
            Call<ConsultationRequestsResponse>

    //FOR SURVEYORS
    @GET("consultation/requests/surveyor")
    fun getSurveyorRequests(
        @Header("Authorization") token: String):
            Call<ConsultationRequestsResponse>

    @POST("consultation/accept/{id}")
    fun acceptConsultationRequest(@Path("id") requestId: String, @Header("Authorization") token: String): Call<ConsultationAcceptDeclineResponse>

    @POST("consultation/decline/{id}")
    fun declineConsultationRequest(@Path("id") requestId: String, @Header("Authorization") token: String): Call<ConsultationAcceptDeclineResponse>


    //FOR ADMIN
    @GET("admin/dashboard")
    fun getAdminDashboard(
        @Header("Authorization") token: String
    ): Call<DashboardResponse>

    @POST("admin/postUpdate")
    fun postAdminUpdate(
        @Header("Authorization") token: String,
        @Body update: Update): Call<UpdatePostResponse>

    @PUT("admin/editUpdate/{id}")
    fun editAdminUpdate(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body update: Update): Call<UpdatePostResponse>

    @DELETE("admin/deleteUpdate/{id}")
    fun deleteAdminUpdate(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ):Call<ResponseBody>

    @GET("admin/consultation-logs")
    fun getLogs(
        @Header("Authorization") token: String
    ): Call<ConsultationLogResponse>

    @GET("admin/getAllUsers")
    fun getAllUsers(
        @Header("Authorization") token: String
    ): Call<UserResponse>

    @DELETE("admin/deleteUser/{id}")
    fun deleteUser(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    companion object{
        operator  fun invoke(): MyApi{
            return Retrofit.Builder()
                .baseUrl("https://terramongodbapi.onrender.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}