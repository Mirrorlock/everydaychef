package com.everydaychef.main.services

import com.everydaychef.main.models.Family
import com.everydaychef.main.models.User
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @GET("user")
    fun getUsers(): Call<List<User>>

    @GET("user/{id}")
    fun getUser(@Path("id") userId: Int): Call<User>

    @GET("user/name/{username}")
    fun getUserByUsername(@Path("username") username: String): Call<User>

//    @FormUrlEncoded
//    @POST("authenticate")
//    fun authenticate(@Field("username") username: String,
//                     @Field("password") password: String): Call<String> // ACQUIRE TOKEN
//
//    @POST("user")
//    fun create(@Query("username") username: String,
//               @Query("email") email: String,
//               @Query("authentication_method") authenticationMethod: Char): Call<Any>@POST("user")
    @POST("user")
    fun create(@Body body: HashMap<String, Any>): Call<Any>

    @DELETE("user/{userId}/family")
    fun leaveFamily(@Path("userId") userId: Int): Call<Family>
//
//    @POST("user")
//    fun create(@Query("username") username: String, @Query("password") password: String,
//               @Query("email") email: String): Call<Any>
}