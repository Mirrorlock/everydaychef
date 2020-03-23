package com.everydaychef.main.models

import com.google.gson.annotations.SerializedName

class Device constructor(private val token: String = ""){
    @SerializedName("id")
    var id = 0

    @SerializedName("firebaseToken")
    var firebaseToken = token

    override fun toString(): String {
        return "Device(id=$id, firebaseToken='$firebaseToken')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Device

        if (token != other.token) return false
        if (id != other.id) return false
        if (firebaseToken != other.firebaseToken) return false

        return true
    }

    override fun hashCode(): Int {
        var result = token.hashCode()
        result = 31 * result + id
        result = 31 * result + firebaseToken.hashCode()
        return result
    }


}