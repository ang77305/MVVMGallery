package example.fiona.pixabay.di.component

import dagger.Component
import example.fiona.pixabay.data.repository.ImageRepository
import example.fiona.pixabay.di.module.ApiHelperModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiHelperModule::class])
interface ApiHelperComponent {
    fun inject(mainRepository: ImageRepository)
}
