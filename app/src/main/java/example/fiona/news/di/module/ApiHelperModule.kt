package example.fiona.pixabay.di.module

import dagger.Module
import dagger.Provides
import example.fiona.pixabay.data.api.ApiHelper
import javax.inject.Singleton

@Module
class ApiHelperModule {

    @Singleton
    @Provides
    fun providesApiHelper(): ApiHelper {
        return ApiHelper()
    }
}