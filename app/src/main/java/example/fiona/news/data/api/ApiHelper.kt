package example.fiona.pixabay.data.api

import example.fiona.pixabay.di.component.ApiComponent
import example.fiona.pixabay.di.component.DaggerApiComponent
import javax.inject.Inject

class ApiHelper {

    @Inject
    lateinit var apiService: ApiService

    init {
        val apiComponent: ApiComponent = DaggerApiComponent.create()
        apiComponent.inject(this)
    }

    fun getImages(input: String, page: Int) = apiService.getImages(input, page)

}