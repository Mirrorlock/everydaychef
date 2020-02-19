package com.everydaychef.auth

import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("user")
    fun getUsers(): Call<List<User>>
}