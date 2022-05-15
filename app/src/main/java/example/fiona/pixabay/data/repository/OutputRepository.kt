package example.fiona.pixabay.data.repository


import io.reactivex.Single
import example.fiona.pixabay.data.model.PixabayData

interface OutputRepository {
    fun getData(searchWord: String, page: Int): Single<PixabayData>
}