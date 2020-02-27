package com.everydaychef.modules

import com.everydaychef.main.services.FamilyService
import com.everydaychef.main.services.IngredientService
import com.everydaychef.main.services.RecipeService
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

    @Provides
    fun provideIngredientService(): IngredientService {
        return retrofit.create(IngredientService::class.java)
    }

    @Provides
    fun provideRecipeService(): RecipeService {
        return retrofit.create(RecipeService::class.java)
    }
}