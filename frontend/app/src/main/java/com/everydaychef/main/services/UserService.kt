package com.everydaychef.main.services

import com.everydaychef.main.models.Family
import com.everydaychef.main.models.User
import com.everydaychef.main.models.helper_models.JwtResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("user")
    fun getUsers(): Call<List<User>>

    @GET("user/{id}")
    fun getUser(@Path("id") userId: Int): Call<User>

    @GET("user/name/{username}")
    fun getUserByUsername(@Path("username") username: String): Call<User>

    @GET("user/{userId}/invitations")
    fun getInvitations(@Path("userId") userId: Int): Call<ArrayList<Family>>

    @POST("user")
    fun create(@Body body: HashMap<String, String>): Call<User>

    @POST("authenticate")
    fun authenticate(@Body body: HashMap<String, String>): Call<JwtResponse>

    @DELETE("user/{userId}/family")
    fun leaveFamily(@Path("userId") userId: Int): Call<Family>

    @PUT("user/{userId}/family/{familyId}/invite")
    fun inviteUserToFamily(@Path("userId") userId: Int, @Path("familyId") familyId: Int,
                   @Body body: HashMap<String, Any>): Call<String>


    @PUT("user/{userId}/family/{familyId}/answer_invitation")
    fun answerInvitation(@Path("userId") userId: Int, @Path("familyId") familyId: Int,
                    @Body body: HashMap<String, Boolean>): Call<User>

//    @POST("user")
//    fun create(@Query("username") username: String, @Query("password") password: String,
//               @Query("email") email: String): Call<Any>
}