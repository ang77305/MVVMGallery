package example.fiona.pixabay.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import example.fiona.pixabay.data.model.PixabayHitsData
import example.fiona.pixabay.data.repository.OutputRepository
import example.fiona.pixabay.di.component.DaggerImageRepositoryComponent
import example.fiona.pixabay.di.component.ImageRepositoryComponent
import example.fiona.pixabay.utils.Resource
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class MainViewModel : ViewModel() {
    var searchWord = ObservableField<String>()
    var page: Int = 1

    @Inject
    lateinit var imagesRepository: OutputRepository

    private val allImages = MutableLiveData<Resource<List<PixabayHitsData>>>()
    private val innerList = ArrayList<PixabayHitsData>()

    private val compositeDisposable = CompositeDisposable()
    private val resetPage = MutableLiveData<Boolean>()
    private val emptyList = MutableLiveData<Boolean>()
    private var gridDisplay = MutableLiveData<Boolean>()

    init {
        val imageRepoComponent: ImageRepositoryComponent = DaggerImageRepositoryComponent.create()
        imageRepoComponent.inject(this)
        searchWord.set("sky")
        fetchImages("sky", true)
        gridDisplay.value=true
    }

    fun fetchImages(input: String, newWord: Boolean) {
        if (newWord) {
            page = 1
            resetPage.postValue(true)
            innerList.clear()
        }

        allImages.postValue(Resource.loading(null))

        compositeDisposable.add(
            imagesRepository.getData(input, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ imageList ->
                    innerList.addAll(imageList.hits)
                    allImages.value = Resource.success(innerList)
                    allImages.postValue(allImages.value)
                    if (page == 1 && imageList.hits.size == 0) {
                        emptyList.postValue(true)
                    } else {
                        emptyList.postValue(false)
                    }
                }, { throwable ->
                    var message = ""
                    message =
                        if (throwable is UnknownHostException || throwable is ConnectException)
                            "Check your internet connection and try again!"
                        else
                            "Something went wrong. try again!"

                    allImages.postValue(
                        Resource.error(
                            message,
                            null
                        )
                    )
                })
        )
    }

    fun loadMore(input: String) {
        page++
        fetchImages(input, false)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


    fun getImages(): LiveData<Resource<List<PixabayHitsData>>> {
        return allImages
    }

    fun getPageStatus(): LiveData<Boolean> {

        return resetPage
    }

    fun getListStatus(): LiveData<Boolean> {

        return emptyList
    }


    fun changeDisplayType() {
        gridDisplay.value = gridDisplay.value?.not()
    }

    fun getDisplayType(): LiveData<Boolean> {
        return gridDisplay
    }

}