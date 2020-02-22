package com.everydaychef.main.services

import com.everydaychef.main.models.Family
import com.everydaychef.main.models.User
import retrofit2.Call
import retrofit2.http.*

interface FamilyService {

    @GET("family/{id}/members")
    fun getMembers(@Path("id") id: Int): Call<Set<User>>

    @PUT("user/{userId}/family/{familyName}/change")
    fun changeUserFamily(@Path("userId") userId: Int,
                         @Path("familyName") familyName: String) : Call<Family>
}