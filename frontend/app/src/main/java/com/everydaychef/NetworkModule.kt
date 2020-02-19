package com.everydaychef

import com.everydaychef.auth.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule{
    @Provides
    fun provideRetrofitService(): UserService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.15/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)
    }
}