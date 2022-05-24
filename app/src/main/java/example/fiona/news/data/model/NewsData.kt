package example.fiona.pixabay.data.model

import com.google.gson.annotations.SerializedName

data class PixabayHitsData(
    @SerializedName("webformatURL")
    val webformatURL: String = "",

    @SerializedName("largeImageURL")
    val largeImageURL: String = "",

    @SerializedName("user")
    val user: String = "",

    @SerializedName("tags")
    val tags: String = "",

    @SerializedName("likes")
    val likes: Int = 0,

    @SerializedName("favorites")
    val favorites: Int = 0,

    @SerializedName("comments")
    val comments: Int = 0,

    @SerializedName("userImageURL")
    val userImageURL: String = ""
)
