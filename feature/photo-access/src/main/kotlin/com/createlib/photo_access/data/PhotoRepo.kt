package com.createlib.photo_access.data

import android.content.Context
import android.net.Uri
import com.createlib.pic_lib.cache.CacheManager
import com.createlib.pic_lib.db.PhotoDao
import com.createlib.pic_lib.db.model.PhotoEntity
import com.createlib.pic_lib.photo.model.Photo
import com.createlib.pic_lib.photo.model.PhotoMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.core.net.toUri

class PhotoRepo @Inject constructor(
    private val photoDao: PhotoDao,
    private val cacheManager: CacheManager,
    private val context: Context
) {

    suspend fun selectAndStorePhotos(uris: List<Uri>): Flow<Result<List<Photo>>> {
        val photos = uris.map { uri ->
            Photo(
                uri = uri,
                metadata = PhotoMetadata(
                    name = uri.toString().substringAfterLast("/"),
                    size = context.contentResolver.openInputStream(uri)?.available()?.toLong() ?: 0L
                )
            )
        }
        photos.forEach { photo ->
            photoDao.insertPhoto(
                PhotoEntity(
                    uri = photo.uri.toString(),
                    metadata = photo.metadata.toString(),
                    timestamp = photo.timestamp
                )
            )
            cacheManager.cachePhoto(photo)
        }

        return flowOf(Result.success(photos))
    }

    fun getALlPhotos(): Flow<List<Photo>> = photoDao.getAllPhotos().map { entities ->
        entities.map { photoEntity ->
            Photo(
                uri = photoEntity.uri.toUri(),
                metadata = PhotoMetadata(photoEntity.metadata),
                timestamp = photoEntity.timestamp
            )
        }
    }
}