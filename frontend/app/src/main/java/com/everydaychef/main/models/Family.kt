package com.everydaychef.main.models

import com.google.gson.annotations.SerializedName

class Family{
    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name = ""

    override fun toString(): String {
        return "Family(id=$id, name='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Family

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }


}