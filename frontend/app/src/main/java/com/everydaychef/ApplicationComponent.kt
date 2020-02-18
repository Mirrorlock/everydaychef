package com.everydaychef

import com.everydaychef.auth.LoginActivity
import com.everydaychef.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: LoginActivity)
//    fun viewModel(userRepository: UserRepository): LoginViewModel
//    fun repository(): UserRepository
}