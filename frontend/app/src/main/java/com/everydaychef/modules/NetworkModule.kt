package com.everydaychef.modules

import com.everydaychef.EverydayChefApplication
import com.everydaychef.main.services.*
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
        .baseUrl(EverydayChefApplication.API_BASE_URL)
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

    @Provides
    fun provideShoppingListService(): ShoppingListService {
        return retrofit.create(ShoppingListService::class.java)
    }
}