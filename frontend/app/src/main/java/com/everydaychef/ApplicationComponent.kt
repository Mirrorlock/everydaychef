package com.everydaychef

import com.everydaychef.auth.LoginActivity
import com.everydaychef.auth.RegisterActivity
import com.everydaychef.main.MainActivity
import com.everydaychef.main.ui.fridge.FridgeFragment
import com.everydaychef.main.ui.home.HomeFragment
import com.everydaychef.main.ui.notifications.NotificationsFragment
import com.everydaychef.main.ui.profile.ProfileFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: RegisterActivity)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: NotificationsFragment)
    fun inject(fragment: FridgeFragment)
}