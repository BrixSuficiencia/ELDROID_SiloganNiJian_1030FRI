package com.sabal.terramasterhub.data.network

import com.sabal.terramasterhub.ui.auth.ProfileUpdateRequest
import com.sabal.terramasterhub.ui.home.ConsultationRequest
import com.sabal.terramasterhub.ui.home.ConsultationRequestSurveyor
import com.sabal.terramasterhub.ui.home.Expert
import com.sabal.terramasterhub.ui.home.ExpertsResponse
import com.sabal.terramasterhub.ui.home.RequestUpdate
import com.sabal.terramasterhub.ui.home.RequestsResponse
import com.sabal.terramasterhub.ui.home.SurveyorsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
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
        @Field("license_number") license_number:String,
        @Field("pricing") pricing : Int
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
        @Body consultationRequest: ConsultationRequest,
        @Header("Authorization") authToken: String
    ): Call<ResponseBody>

    @POST("request-consultation/surveyor")
    fun sendConsultationRequestSurveyor(
        @Body consultationRequest: ConsultationRequestSurveyor,
        @Header("Authorization") authToken: String
    ): Call<ResponseBody>

    @GET("consultation/getAllRequests")
    fun getAllRequests(@Header("Authorization")
    authToken: String): Call<RequestsResponse>

    // PUT request to update consultation request
    @PUT("consultation/updateRequest/{id}")
    fun updateRequest(
        @Header("Authorization") authToken: String,
        @Path("id") id: String,           // Request ID
        @Body requestUpdate: RequestUpdate // Body of the request
    ): Call<ResponseBody>

    @DELETE("consultation/deleteRequest/{id}")
    fun deleteRequest(
        @Header("Authorization") authToken: String,
        @Path("id") id: String) : Call<ResponseBody>

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