package example.fiona.pixabay.di.component

import dagger.Component
import example.fiona.pixabay.di.module.ImageRepositoryModule
import example.fiona.pixabay.ui.viewmodel.MainViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [ImageRepositoryModule::class])
interface ImageRepositoryComponent {
    fun inject(imagesViewModel: MainViewModel)
}
