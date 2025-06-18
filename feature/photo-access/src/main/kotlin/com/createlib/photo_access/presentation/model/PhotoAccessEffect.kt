package com.createlib.photo_access.presentation.model

sealed class PhotoAccessEffect {
    data class ShowError(val message: String) : PhotoAccessEffect()
    data object NavigateToMl : PhotoAccessEffect()
}