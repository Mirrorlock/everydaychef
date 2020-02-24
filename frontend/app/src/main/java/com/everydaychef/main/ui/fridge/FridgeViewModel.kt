package com.everydaychef.main.ui.fridge

import androidx.lifecycle.ViewModel
import com.everydaychef.main.repositories.FamilyRepository
import com.everydaychef.main.repositories.UserRepository
import javax.inject.Inject

class FridgeViewModel @Inject constructor(private val userRepository: UserRepository,
                                  private val familyRepository: FamilyRepository): ViewModel() {


}