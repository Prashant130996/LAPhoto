package com.createlib.photo_access.presentation.viewmodel

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.createlib.photo_access.data.PhotoRepo
import com.createlib.photo_access.presentation.model.PhotoAccessEffect
import com.createlib.photo_access.presentation.model.PhotoAccessEvent
import com.createlib.photo_access.presentation.model.PhotoAccessState
import com.createlib.pic_lib.photo.PhotoAccessHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoAccessViewModel @Inject constructor(
    private val photoRepo: dagger.Lazy<PhotoRepo>
) : ViewModel() {

    private val _state = MutableStateFlow(PhotoAccessState())
    val state: StateFlow<PhotoAccessState> = _state.asStateFlow()

    private val _effect = Channel<PhotoAccessEffect>()
    val effect: Flow<PhotoAccessEffect> = _effect.receiveAsFlow()

    fun onEvent(event: PhotoAccessEvent) {
        when (event) {
            is PhotoAccessEvent.OnRequestPermission -> requestPhotos(event.activity)
            is PhotoAccessEvent.OnPhotoSelected -> processPhotos(event.uris)
            is PhotoAccessEvent.OnPermissionDenied -> {
                _state.update { it.copy(isPermissionDenied = true) }
                viewModelScope.launch {
                    _effect.send(PhotoAccessEffect.ShowError("Photo Access Denied"))
                }
            }
        }
    }

    private fun processPhotos(uris: List<Uri>) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            photoRepo.get().selectAndStorePhotos(uris).collect { result ->
                result.onSuccess { photos ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            photos = photos,
                            isPermissionDenied = false
                        )
                    }
                    //_effect.send(PhotoAccessEffect.NavigateToMl)
                }.onFailure { throwable ->
                    _state.update { it.copy(isLoading = false, error = throwable.message) }
                    _effect.send(
                        PhotoAccessEffect.ShowError(
                            throwable.message ?: "Error processing photos"
                        )
                    )
                }

            }
        }
    }

    private fun requestPhotos(activity: ComponentActivity) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            PhotoAccessHelper.requestPhotoAccess(
                activity = activity,
                onPhotosSelected = { uris ->
                    onEvent(PhotoAccessEvent.OnPhotoSelected(uris))
                },
                onDenied = {
                    onEvent(PhotoAccessEvent.OnPermissionDenied)
                }
            )
        }
    }
}