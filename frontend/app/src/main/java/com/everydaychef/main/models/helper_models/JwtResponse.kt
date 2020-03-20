package com.everydaychef.main.models.helper_models

import com.google.gson.annotations.SerializedName

class JwtResponse{
    @SerializedName("token")
    var jwtToken: String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JwtResponse

        if (jwtToken != other.jwtToken) return false

        return true
    }

    override fun hashCode(): Int {
        return jwtToken.hashCode()
    }

    override fun toString(): String {
        return "JwtResponse(jwtToken='$jwtToken')"
    }


}