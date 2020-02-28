package com.everydaychef.modules

import com.everydaychef.main.services.FamilyService
import com.everydaychef.main.services.IngredientService
import com.everydaychef.main.services.RecipeService
import com.everydaychef.main.services.UserService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule{
    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build();

    private val retrofit = Retrofit.Builder()
//        .baseUrl("https://everydaychef.home.kutiika.net/")
        .baseUrl("http://192.168.1.2/")
        .client(okHttpClient)
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

    @Provides
    fun provideIngredientService(): IngredientService {
        return retrofit.create(IngredientService::class.java)
    }

    @Provides
    fun provideRecipeService(): RecipeService {
        return retrofit.create(RecipeService::class.java)
    }
}