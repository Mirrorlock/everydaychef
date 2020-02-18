package com.everydaychef.auth

class CurrentUser{
    var username: String = ""
    var token: String = ""
    var email: String = ""
    var photoUrl: String = ""

    override fun toString(): String {
        return "CurrentUser(username='$username', token='$token', email='$email', photoUrl='$photoUrl')"
    }

}