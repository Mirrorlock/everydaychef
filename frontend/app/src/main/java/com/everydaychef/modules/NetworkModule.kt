package com.everydaychef.modules

import android.content.Context
import com.everydaychef.EverydayChefApplication
import com.everydaychef.auth.AuthInterceptor
import com.everydaychef.main.repositories.UserRepository
import com.everydaychef.main.services.*
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Module
class NetworkModule{

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(EverydayChefApplication.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

    }

    @Provides
    fun provideConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun provideFamilyService(retrofit: Retrofit): FamilyService{
        return retrofit.create(FamilyService::class.java)
    }

    @Provides
    fun provideIngredientService(retrofit: Retrofit): IngredientService {
        return retrofit.create(IngredientService::class.java)
    }

    @Provides
    fun provideRecipeService(retrofit: Retrofit): RecipeService {
        return retrofit.create(RecipeService::class.java)
    }

    @Provides
    fun provideShoppingListService(retrofit: Retrofit): ShoppingListService {
        return retrofit.create(ShoppingListService::class.java)
    }
}