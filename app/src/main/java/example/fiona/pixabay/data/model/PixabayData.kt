package example.fiona.pixabay.data.model

import com.google.gson.annotations.SerializedName

data class PixabayData(
    @SerializedName("hits")
    val hits: ArrayList<PixabayHitsData>
)