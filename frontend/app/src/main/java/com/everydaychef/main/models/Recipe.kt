package com.everydaychef.main.models

import com.google.gson.annotations.SerializedName

class Recipe {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("picture_url")
    var picture_url: String = ""

    @SerializedName("number_of_likes")
    var number_of_likes = 0

    @SerializedName("creator")
    var creator: User? = null

    @SerializedName("likes")
    var likedUsers: List<User> = ArrayList()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recipe

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (picture_url != other.picture_url) return false
        if (number_of_likes != other.number_of_likes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + picture_url.hashCode()
        result = 31 * result + number_of_likes
        return result
    }

    override fun toString(): String {
        return "Recipe(id=$id, name='$name', description='$description', picture_url='$picture_url', number_of_likes=$number_of_likes)"
    }


}