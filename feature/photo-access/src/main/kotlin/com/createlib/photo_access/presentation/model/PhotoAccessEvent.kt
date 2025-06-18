package com.createlib.photo_access.presentation.model

import android.net.Uri
import androidx.activity.ComponentActivity

sealed class PhotoAccessEvent {
    data class OnPhotoSelected(val uris: List<Uri>) : PhotoAccessEvent()
    data object OnPermissionDenied : PhotoAccessEvent()
    data class OnRequestPermission(val activity: ComponentActivity) : PhotoAccessEvent()
}