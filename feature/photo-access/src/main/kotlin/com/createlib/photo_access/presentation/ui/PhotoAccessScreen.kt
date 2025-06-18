package com.createlib.photo_access.presentation.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.createlib.photo_access.presentation.model.PhotoAccessEffect
import com.createlib.photo_access.presentation.model.PhotoAccessEvent
import com.createlib.photo_access.presentation.viewmodel.PhotoAccessViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PhotoAccessScreen(
    onNavigateToMLProcessing: () -> Unit
) {
    val context = LocalContext.current
    val photoAccessViewModel = hiltViewModel<PhotoAccessViewModel>()
    val state by photoAccessViewModel.state.collectAsState()

    LaunchedEffect(photoAccessViewModel) {
        photoAccessViewModel.effect.collectLatest { effect ->
            when (effect) {
                is PhotoAccessEffect.NavigateToMl -> onNavigateToMLProcessing()
                is PhotoAccessEffect.ShowError -> {
                    // Show Snackbar or Toast
                    // Example: SnackbarHostState().showSnackbar(effect.message)
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                photoAccessViewModel.onEvent(
                    PhotoAccessEvent.OnRequestPermission(
                        activity = context as ComponentActivity
                    )
                )
            }
        ) {
            Text("Select Photos")
        }

        when {
            state.isLoading -> CircularProgressIndicator()

            state.isPermissionDenied -> Text("Photo access was denied. Please try again.")

            state.error != null -> Text("Error: ${state.error}")

            state.photos.isNotEmpty() -> {
                LazyColumn {
                    items(state.photos) { photo ->
                        Text("Selected: ${photo.metadata.name}")
                    }
                }
            }
        }
    }
}