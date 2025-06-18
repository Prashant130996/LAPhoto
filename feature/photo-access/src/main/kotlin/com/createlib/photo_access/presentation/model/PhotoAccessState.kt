package com.createlib.photo_access.presentation.model

import com.createlib.pic_lib.photo.model.Photo

data class PhotoAccessState(
    val photos: List<Photo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPermissionDenied: Boolean = false
)
