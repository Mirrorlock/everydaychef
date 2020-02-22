package com.everydaychef

import com.everydaychef.main.services.FamilyService
import com.everydaychef.main.services.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule{
    private val retrofit = Retrofit.Builder()
//        .baseUrl("https://everydaychef.home.kutiika.net/")
        .baseUrl("http://192.168.1.2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideUserService(): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun provideFamilyService(): FamilyService{
        return retrofit.create(FamilyService::class.java)
    }
}