package com.createlib.pic_lib.photo

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.qualifiers.ApplicationContext

object PhotoAccessHelper {
    fun requestPhotoAccess(
        activity: ComponentActivity,
        onPhotosSelected: (List<Uri>) -> Unit,
        onDenied: () -> Unit
    ) {
        val photoPicker = activity.registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia()
        ) { uris ->
            if (uris.isNotEmpty()) {
                onPhotosSelected(uris)
            } else {
                onDenied()
            }
        }
        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

}