package com.everydaychef.main.models

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name = ""

    @SerializedName("email")
    var email = ""

    @SerializedName("password")
    var password = ""

    @SerializedName("account_type")
    var accountType = ""

    @SerializedName("family")
    var family: Family =
        Family()

    @SerializedName("devices")
    var devices: Set<Device> = HashSet()

    @SerializedName("invitations")
    var invitations: List<Family>? = null

    override fun toString(): String {
        return "User(id=$id, name='$name', email='$email', password='$password', accountType='$accountType', family=$family, invitations=$invitations, firebase_tokens=${devices?.map { it.toString() }})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (name != other.name) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (accountType != other.accountType) return false
        if (family != other.family) return false
        if (invitations != other.invitations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + accountType.hashCode()
        result = 31 * result + family.hashCode()
        result = 31 * result + (invitations?.hashCode() ?: 0)
        return result
    }


}