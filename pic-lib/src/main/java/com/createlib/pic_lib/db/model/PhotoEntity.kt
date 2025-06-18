package com.createlib.pic_lib.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey val uri: String,
    val metadata: String,
    val timestamp: Long
)