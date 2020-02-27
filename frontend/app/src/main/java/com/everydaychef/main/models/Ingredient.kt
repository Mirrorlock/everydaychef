package com.everydaychef.main.models

import com.google.gson.annotations.SerializedName

class Ingredient {
    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("pictureUrl")
    var pictureUrl: String = ""

    @SerializedName("category")
    var category: IngredientCategory = IngredientCategory()

    override fun toString(): String {
        return "Ingredient(id=$id, name='$name', pictureUrl='$pictureUrl', category=$category)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ingredient

        if (id != other.id) return false
        if (name != other.name) return false
        if (pictureUrl != other.pictureUrl) return false
        if (category != other.category) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + pictureUrl.hashCode()
        result = 31 * result + category.hashCode()
        return result
    }


}
