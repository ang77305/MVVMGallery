package example.fiona.pixabay.di.module

import dagger.Module
import dagger.Provides
import example.fiona.pixabay.data.repository.ImageRepository
import example.fiona.pixabay.data.repository.OutputRepository
import javax.inject.Singleton

@Module
class ImageRepositoryModule {

    @Singleton
    @Provides
    fun providesImageRepository(): OutputRepository {
        return ImageRepository()
    }
}