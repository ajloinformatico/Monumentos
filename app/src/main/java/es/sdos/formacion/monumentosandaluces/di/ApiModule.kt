package es.sdos.formacion.monumentosandaluces.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.sdos.formacion.monumentosandaluces.data.api.RetrofitInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    private  const val BASE_URL = "https://run.mocky.io/"

    @Singleton
    @Provides fun provideRetrofit(): RetrofitInterface {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(RetrofitInterface::class.java)
    }
}