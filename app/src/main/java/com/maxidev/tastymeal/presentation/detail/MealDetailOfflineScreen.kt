@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxidev.tastymeal.domain.model.Meal
import kotlinx.coroutines.launch

/* ----- Offline ----- */

@Composable
fun MealDetailScreenOffline(
    viewModel: MealDetailOfflineViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.mealById.collectAsStateWithLifecycle()
    val isBookmark by viewModel.isBookmarked().collectAsStateWithLifecycle(false)

    ScreenContentOffline(
        isBookmarked = isBookmark,
        meal = state ?: return,
        onEvent = { event ->
            when (event) {
                MealDetailUiEvents.NavigateBack -> { navigateBack() }
                is MealDetailUiEvents.AddToBookmark -> {
                    viewModel.saveToBookmark(event.add)
                }
                is MealDetailUiEvents.RemoveToBookmark -> {
                    viewModel.deleteFromBookmark(event.remove)
                }
            }
        }
    )
}

@Composable
private fun ScreenContentOffline(
    isBookmarked: Boolean,
    meal: Meal,
    onEvent: (MealDetailUiEvents) -> Unit
) {
    val context = LocalContext.current
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarState) }) { innerPadding ->
        val tintColor = if (isBookmarked) Color.Red else LocalContentColor.current

        MealDetailContent(
            tint = tintColor,
            navigateBack = { onEvent(MealDetailUiEvents.NavigateBack) },
            onBookmark = {
                if (isBookmarked) {
                    onEvent(
                        MealDetailUiEvents.RemoveToBookmark(
                            remove = meal.copy(bookmarked = false)
                        )
                    )
                    scope.launch {
                        snackbarState.showSnackbar(
                            message = "Removed from bookmarks.",
                            duration = SnackbarDuration.Short
                        )
                    }
                } else {
                    onEvent(
                        MealDetailUiEvents.AddToBookmark(
                            add = meal.copy(bookmarked = true)
                        )
                    )
                    scope.launch {
                        snackbarState.showSnackbar(
                            message = "Saved to bookmarks.",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            },
            onShare = {
                val dataStrSource = meal.strSource
                val sendChooser = Intent.createChooser(
                    Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, dataStrSource)
                        type = "text/plain"
                    }, "Share recipe."
                )

                if (dataStrSource.isNotEmpty()) {
                    context.startActivity(sendChooser)
                } else {
                    scope.launch {
                        snackbarState.showSnackbar(
                            message = "No source available.",
                            duration = SnackbarDuration.Short,
                            actionLabel = "Dismiss"
                        )
                    }
                }
            },
            onLink = {
                val sourceUri = meal.strSource.toUri()
                val browserIntent = Intent(Intent.ACTION_VIEW, sourceUri)

                if (sourceUri != Uri.EMPTY) {
                    context.startActivity(browserIntent)
                } else {
                    scope.launch {
                        snackbarState.showSnackbar(
                            message = "No source available.",
                            duration = SnackbarDuration.Short,
                            actionLabel = "Dismiss"
                        )
                    }
                }
            },
            onWatchInYoutube = {
                val youTubeUri = meal.strYouTube.toUri()
                val youTubeIntent = Intent(Intent.ACTION_VIEW, youTubeUri)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                if (youTubeUri != Uri.EMPTY) {
                    context.startActivity(youTubeIntent)
                } else {
                    scope.launch {
                        snackbarState.showSnackbar(
                            message = "No video available.",
                            duration = SnackbarDuration.Short,
                            actionLabel = "Dismiss"
                        )
                    }
                }
            },
            meal = meal,
            modifier = Modifier.padding(innerPadding)
        )
    }
}