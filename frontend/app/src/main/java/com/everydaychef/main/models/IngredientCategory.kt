package com.everydaychef.main.models

import com.google.gson.annotations.SerializedName

class IngredientCategory{
    @SerializedName("Id")
    var Id = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("pictureUrl")
    var pictureUrl: String = ""



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IngredientCategory

        if (Id != other.Id) return false
        if (name != other.name) return false
        if (pictureUrl != other.pictureUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Id
        result = 31 * result + name.hashCode()
        result = 31 * result + pictureUrl.hashCode()
        return result
    }

    override fun toString(): String {
        return "IngredientCategory(Id=$Id, name='$name', pictureUrl='$pictureUrl')"
    }


}