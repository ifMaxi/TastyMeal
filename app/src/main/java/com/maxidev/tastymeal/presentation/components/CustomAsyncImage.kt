package com.maxidev.tastymeal.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage

@Composable
fun CustomAsyncImage(
    modifier: Modifier = Modifier,
    model: String,
    // Custom options
    contentDescription: String? = "",
    contentScale: ContentScale = ContentScale.Fit,
    height: Dp = Dp.Unspecified,
    onClick: () -> Unit = {}
) {
    // TODO: Add shimmer or placeholder
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .height(height)
            .clickable { onClick() }
    )
}