package example.fiona.pixabay.data.api

import io.reactivex.Single
import example.fiona.pixabay.BuildConfig
import example.fiona.pixabay.data.model.PixabayData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(
        "?key=" + BuildConfig.API_CREDENTIAL + "&image_type=photo&pretty=true"
    )
    fun getImages(
        @Query("q") input: String?,
        @Query("page") page: Int
    ): Single<PixabayData>

}