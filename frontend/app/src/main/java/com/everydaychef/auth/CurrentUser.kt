package com.everydaychef.auth

import com.everydaychef.main.models.User

class CurrentUser{

    var id: Int = 0
    var user: User = User()
    var username: String = ""
    var email: String = ""
    var photoUrl: String = ""

    override fun toString(): String {
        return "CurrentUser(username='$username', email='$email', photoUrl='$photoUrl')"
    }

}