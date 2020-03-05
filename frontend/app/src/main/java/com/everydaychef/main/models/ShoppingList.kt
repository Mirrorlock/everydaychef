package com.everydaychef.main.models

import com.google.gson.annotations.SerializedName

class ShoppingList {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("creator_family")
    var creatorFamily: Family = Family()

    @SerializedName("ingredients")
    var ingredients: List<Ingredient> = ArrayList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShoppingList

        if (id != other.id) return false
        if (name != other.name) return false
        if (creatorFamily != other.creatorFamily) return false
        if (ingredients != other.ingredients) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + creatorFamily.hashCode()
        result = 31 * result + ingredients.hashCode()
        return result
    }

    override fun toString(): String {
        return "ShoppingList(id=$id, name='$name', creatorFamily=$creatorFamily, ingredients=$ingredients)"
    }


}