package com.createlib.pic_lib.photo.model;

import android.net.Uri;

data class Photo(
    val uri: Uri,
    val metadata: PhotoMetadata = PhotoMetadata(),
    val timestamp: Long = System.currentTimeMillis()
)

data class PhotoMetadata(
    val name: String = "",
    val size: Long = 0L
)