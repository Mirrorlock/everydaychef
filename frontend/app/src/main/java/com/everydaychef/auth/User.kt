package com.everydaychef.auth

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name = ""

    @SerializedName("password")
    var password = ""

    @SerializedName("account_type")
    var accountType = ""

    @SerializedName("family")
    var family: Family = Family()

    @SerializedName("invitations")
    var invitations: List<String>? = null

    override fun toString(): String {
        return "User(id=$id, name='$name', password='$password', accountType='$accountType', family=$family, invitations=$invitations)"
    }


}

class Family{
    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name = ""

    override fun toString(): String {
        return "Family(id=$id, name='$name')"
    }

}