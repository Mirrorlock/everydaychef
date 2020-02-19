package com.everydaychef.auth

import dagger.Module
import dagger.Provides
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET

@Module
class NetworkModule{
    @Provides
    fun provideRetrofitService(): UserService{
        return Retrofit.Builder()
            .baseUrl("something")
            .build()
            .create(UserService::class.java)
    }
}