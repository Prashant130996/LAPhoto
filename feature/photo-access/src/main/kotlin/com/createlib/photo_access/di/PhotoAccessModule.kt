package com.createlib.photo_access.di

import android.content.Context
import com.createlib.photo_access.data.PhotoRepo
import com.createlib.pic_lib.cache.CacheManager
import com.createlib.pic_lib.db.PhotoDao
import com.createlib.pic_lib.db.PhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhotoAccessModule {

    /*@Provides
    @Singleton
    fun provideDb(context: Context): PhotoDatabase {
        return Room.databaseBuilder(
            context,
            PhotoDatabase::class.java, "photo-database"
        ).build()
    }*/

    @Provides
    @Singleton
    fun providePhotoDao(photoDatabase: PhotoDatabase): PhotoDao {
        return photoDatabase.photoDao()
    }

    @Provides
    @Singleton
    fun providePhotoRepo(
        photoDao: PhotoDao,
        cacheManager: CacheManager,
        context: Context
    ): PhotoRepo {
        return PhotoRepo(photoDao, cacheManager,context)
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext applicationContext: Context): Context =
        applicationContext
}