package com.everydaychef.main.models

import com.google.gson.annotations.SerializedName

class IngredientCategory{
    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("pictureUrl")
    var pictureUrl: String = ""



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IngredientCategory

        if (id != other.id) return false
        if (name != other.name) return false
        if (pictureUrl != other.pictureUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + pictureUrl.hashCode()
        return result
    }

    override fun toString(): String {
        return "IngredientCategory(Id=$id, name='$name', pictureUrl='$pictureUrl')"
    }


}