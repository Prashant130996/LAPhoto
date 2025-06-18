package com.createlib.pic_lib.db

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import com.createlib.pic_lib.db.model.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Database(entities = [PhotoEntity::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}

@Dao
interface PhotoDao {

    @Insert
    suspend fun insertPhoto(photo: PhotoEntity)

    @WorkerThread
    @Query("SELECT * FROM photos")
    fun getAllPhotos(): Flow<List<PhotoEntity>>

    @Query("DELETE FROM photos WHERE timestamp < :threshold")
    suspend fun evictOldPhotos(threshold: Long)
}