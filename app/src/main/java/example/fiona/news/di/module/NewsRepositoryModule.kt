package example.fiona.news.di.module

import dagger.Module
import dagger.Provides
import example.fiona.news.data.repository.NewsRepository
import javax.inject.Singleton

@Module
class ImageRepositoryModule {

    @Singleton
    @Provides
    fun providesImageRepository(): OutputRepository {
        return NewsRepository()
    }
}