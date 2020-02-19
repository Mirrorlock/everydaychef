package com.everydaychef

import com.everydaychef.auth.LoginActivity
import com.everydaychef.main.MainActivity
import com.everydaychef.main.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: LoginActivity)
    fun inject(fragment: HomeFragment)

//    fun viewModel(userRepository: UserRepository): LoginViewModel
//    fun repository(): UserRepository
}