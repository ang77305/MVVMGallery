package example.fiona.news.di.component

import dagger.Component
import example.fiona.news.di.module.ImageRepositoryModule
import example.fiona.news.ui.viewmodel.MainViewModel
import javax.inject.Singleton


@Singleton
@Component(modules = [ImageRepositoryModule::class])
interface ImageRepositoryComponent {
    fun inject(imagesViewModel: MainViewModel)
}
