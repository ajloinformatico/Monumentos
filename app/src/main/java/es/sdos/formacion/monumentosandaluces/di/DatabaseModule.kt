package es.sdos.formacion.monumentosandaluces.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.sdos.formacion.monumentosandaluces.data.dao.ImageDAO
import es.sdos.formacion.monumentosandaluces.data.dao.MonumentDAO
import es.sdos.formacion.monumentosandaluces.data.database.AppDataBase
import es.sdos.formacion.monumentosandaluces.data.database.AppDataBase_Impl
import es.sdos.formacion.monumentosandaluces.util.Util
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {


    //Singleton instance and methodProvide
    @Provides
    @Singleton
    fun databaseProvider(@ApplicationContext appContext: Context): AppDataBase =
        Room.databaseBuilder(
            appContext,
            AppDataBase::class.java,
            Util.DATABASE_NAME
        ).build()


    @Provides
    @Singleton
    fun provideMonumentDAO(dataBase: AppDataBase): MonumentDAO = dataBase.monumentDao()

    @Provides
    @Singleton
    fun provideImageDAO(dataBase: AppDataBase): ImageDAO = dataBase.imageDao()

}