package com.everydaychef

import com.everydaychef.auth.LoginActivity
import com.everydaychef.auth.RegisterActivity
import com.everydaychef.main.MainActivity
import com.everydaychef.main.ui.cook.CookFragment
import com.everydaychef.main.ui.fridge.FridgeFragment
import com.everydaychef.main.ui.home.HomeFragment
import com.everydaychef.main.ui.notifications.NotificationsFragment
import com.everydaychef.main.ui.profile.ProfileFragment
import com.everydaychef.modules.AppModule
import com.everydaychef.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: RegisterActivity)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: NotificationsFragment)
    fun inject(fragment: FridgeFragment)
    fun inject(fragment: CookFragment)
}