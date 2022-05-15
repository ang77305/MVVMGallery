package example.fiona.pixabay.di.component

import dagger.Component
import example.fiona.pixabay.data.api.ApiHelper
import example.fiona.pixabay.di.module.ApiModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(apiHelper: ApiHelper)
}
