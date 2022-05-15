package example.fiona.pixabay.data.repository

import io.reactivex.Single
import example.fiona.pixabay.data.api.ApiHelper
import example.fiona.pixabay.data.model.PixabayData
import example.fiona.pixabay.di.component.ApiHelperComponent
import example.fiona.pixabay.di.component.DaggerApiHelperComponent

import javax.inject.Inject


class ImageRepository : OutputRepository {

    @Inject
    lateinit var apiHelper: ApiHelper

    init {
        val apiHelperComponent: ApiHelperComponent = DaggerApiHelperComponent.create();
        apiHelperComponent.inject(this)
    }

    override fun getData(searchWord: String, page: Int): Single<PixabayData> {

        return apiHelper.getImages(searchWord, page)

    }
}